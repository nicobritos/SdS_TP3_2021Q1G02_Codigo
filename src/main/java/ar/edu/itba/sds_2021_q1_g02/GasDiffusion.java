package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.serializer.Serializer;
import javafx.util.Pair;

import java.util.*;

public class GasDiffusion {
    private final Dimen dimen;
    private final List<Particle> particles;
    private final List<Serializer> serializers;

    public GasDiffusion(List<Particle> particles, Dimen dimen) {
        this.particles = particles;
        this.dimen = dimen;
        this.serializers = new LinkedList<>();
    }

    public void addSerializer(Serializer serializer) {
        this.serializers.add(serializer);
    }

    public void simulate(int maxIterations) {
        int i = 0;

        Step step = this.calculateFirstStep();
        while (i < maxIterations) {
            step = this.simulateStep(step.getNextEvents(), step.getParticleNextEvent());
            this.serialize(i, step.getMinTime());
            i++;
        }
    }


    private Step simulateStep(TreeMap<Double, Set<Event>> nextEvents, Map<Particle, Event> particleNextEvent) {
        Map.Entry<Double, Set<Event>> firstEvents = nextEvents.firstEntry();
        // Mover particulas a primer proximo evento
        Equations.getInstance().evolveParticlesPositions(this.particles, firstEvents.getKey());

        TreeMap<Double, Set<Event>> newNextEvents = new TreeMap<>(nextEvents);
        Map<Particle, Event> newParticleNextEvent = new HashMap<>(particleNextEvent);

        for (Event event : firstEvents.getValue()) {
            // Colisionar
            if (event.collidesWithWall()) {
                Velocity newVelocity = Equations.getInstance().evolveParticleVelocity(event.getParticle(), event.getWallDirection());
                event.getParticle().setVelocity(newVelocity);
            } else {
                Pair<Velocity, Velocity> velocities = Equations.getInstance().evolveParticlesVelocities(event.getParticle(), event.getOtherParticle());
                event.getParticle().setVelocity(velocities.getKey());
                event.getOtherParticle().setVelocity(velocities.getValue());
            }
        }

        // Copiamos el mapa y no iteramos sobre el mismo set para no
        // eliminar el mismo evento mientras se itera
        for (Map.Entry<Double, Set<Event>> entry : nextEvents.entrySet()) {
            newNextEvents.put(entry.getKey(), new TreeSet<>(entry.getValue()));
        }

        Set<Event> newNextEvent = newNextEvents.get(firstEvents.getKey());
        for (Event event : firstEvents.getValue()) {
            // El evento ya paso
            newNextEvent.remove(event);
            if (newNextEvent.isEmpty())
                newNextEvents.remove(event.getTime());

            // Tengo que buscar proximos eventos con las particulas que
            // interactuaron en la colision y eliminarlos
            Event particleEvent = newParticleNextEvent.get(event.getParticle());
            newParticleNextEvent.remove(event.getParticle());
            Set<Event> particleEvents = newNextEvents.get(particleEvent.getTime());
            if (particleEvents != null) {
                particleEvents.remove(particleEvent);
                if (particleEvents.isEmpty())
                    newNextEvents.remove(particleEvent.getTime());
            }

            if (!event.collidesWithWall()) {
                Event otherParticleEvent = newParticleNextEvent.get(event.getOtherParticle());
                newParticleNextEvent.remove(event.getOtherParticle());
                Set<Event> otherParticleEvents = newNextEvents.get(otherParticleEvent.getTime());
                if (otherParticleEvents != null) {
                    otherParticleEvents.remove(particleEvent);
                    if (otherParticleEvents.isEmpty()) {
                        newNextEvents.remove(otherParticleEvent.getTime());
                    }
                }
            }

            // Recalculamos proximos eventos de las particulas colisionadas
            Event newEvent = this.getEvent(event.getParticle());
            if (newEvent != null) {
                newParticleNextEvent.put(newEvent.getParticle(), newEvent);
                if (!newEvent.collidesWithWall())
                    newParticleNextEvent.put(newEvent.getOtherParticle(), newEvent);

                newNextEvents.computeIfAbsent(newEvent.getTime(), time -> new HashSet<>()).add(newEvent);
            }
            if (!event.collidesWithWall()) {
                Event newOtherEvent = this.getEvent(event.getOtherParticle());
                if (newOtherEvent != null && !newOtherEvent.equalsInverse(newEvent)) {
                    newParticleNextEvent.put(newOtherEvent.getParticle(), newOtherEvent);
                    newNextEvents.computeIfAbsent(newOtherEvent.getTime(), time -> new HashSet<>()).add(newOtherEvent);
                }
            }
        }

        return new Step(newNextEvents, newParticleNextEvent, firstEvents.getKey());
    }

    private Step calculateFirstStep() {
        TreeMap<Double, Set<Event>> nextEvents = new TreeMap<>();
        Map<Particle, Event> particleNextEvent = new HashMap<>();

        // Put eventos
        for (Particle p : this.particles) {
            Event event = this.getEvent(p);
            if (event == null)
                continue;

            if (!event.collidesWithWall()) {
                Set<Event> setEvents = nextEvents.get(event.getTime());
                if (setEvents != null) {
                    if (!setEvents.contains(new Event(event.getTime(), event.getOtherParticle(), event.getParticle()))) {
                        setEvents.add(event);
                    }
                } else {
                    nextEvents.computeIfAbsent(event.getTime(), time -> new HashSet<>()).add(event);
                }
            } else {
                nextEvents.computeIfAbsent(event.getTime(), time -> new HashSet<>()).add(event);
            }

            particleNextEvent.put(event.getParticle(), event);
            if (!event.collidesWithWall()) {
                particleNextEvent.put(event.getOtherParticle(), event);
            }
        }

        return new Step(nextEvents, particleNextEvent, 0);
    }

    private Event getEvent(Particle particle) {
        Pair<Double, Direction> wallCollision = Equations.getInstance().collisionWall(particle, this.dimen);
        Pair<Double, Particle> particleCollision = Equations.getInstance().collisionParticles(particle, this.particles);

        if (wallCollision.getKey() == Double.POSITIVE_INFINITY && particleCollision.getKey() == Double.POSITIVE_INFINITY)
            return null;

        if (wallCollision.getKey() < particleCollision.getKey()) {
            return new Event(wallCollision.getKey(), particle, wallCollision.getValue());
        } else {
            return new Event(particleCollision.getKey(), particle, particleCollision.getValue());
        }
    }

    private void serialize(int step, double dt) {
        for (Serializer serializer : this.serializers) {
            serializer.serialize(this.particles, step, dt, this.dimen);
        }
    }

    private static class Step {
        private final TreeMap<Double, Set<Event>> nextEvents;
        private final Map<Particle, Event> particleNextEvent;
        private final double minTime;

        public Step(TreeMap<Double, Set<Event>> nextEvents, Map<Particle, Event> particleNextEvent, double minTime) {
            this.nextEvents = nextEvents;
            this.particleNextEvent = particleNextEvent;
            this.minTime = minTime;
        }

        public TreeMap<Double, Set<Event>> getNextEvents() {
            return this.nextEvents;
        }

        public Map<Particle, Event> getParticleNextEvent() {
            return this.particleNextEvent;
        }

        public double getMinTime() {
            return this.minTime;
        }
    }
}
