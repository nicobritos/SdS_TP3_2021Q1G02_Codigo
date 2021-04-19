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
        this.serializeSystem();
        Step step = this.calculateFirstStep();
        while (step.getStep() < maxIterations) {
            step = this.simulateStep(step);
            this.serialize(step.getStep() - 1, step.getRelativeTime(), step.getAbsoluteTime());
        }
    }

    private Step simulateStep(Step previousStep) {
        Map.Entry<Double, Set<Event>> firstEvents = previousStep.getNextEvents().firstEntry();
        double absoluteTime = firstEvents.getKey();

        // Mover particulas a primer proximo evento
        Equations.getInstance().evolveParticlesPositions(this.particles, absoluteTime - previousStep.getAbsoluteTime());

        TreeMap<Double, Set<Event>> newNextEvents = new TreeMap<>(previousStep.getNextEvents());
        Map<Particle, Event> newParticleNextEvent = new HashMap<>(previousStep.getParticleNextEvent());

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
        for (Map.Entry<Double, Set<Event>> entry : previousStep.getNextEvents().entrySet()) {
            newNextEvents.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }

        Set<Event> newNextEvent = newNextEvents.get(firstEvents.getKey());
        for (Event event : firstEvents.getValue()) {
            // El evento ya paso
            newNextEvent.remove(event);
            newNextEvent.remove(event.getInverse());
            if (newNextEvent.isEmpty())
                newNextEvents.remove(event.getTime());

            // Tengo que buscar proximos eventos con las particulas que
            // interactuaron en la colision y eliminarlos
            Event particleEvent = newParticleNextEvent.get(event.getParticle());
            newParticleNextEvent.remove(event.getParticle());
            Set<Event> particleEvents = newNextEvents.get(particleEvent.getTime());
            if (particleEvents != null) {
                particleEvents.remove(particleEvent);
                particleEvents.remove(particleEvent.getInverse());
                if (particleEvents.isEmpty())
                    newNextEvents.remove(particleEvent.getTime());
            }

            if (!event.collidesWithWall()) {
                Event otherParticleEvent = newParticleNextEvent.get(event.getOtherParticle());
                newParticleNextEvent.remove(event.getOtherParticle());
                Set<Event> otherParticleEvents = newNextEvents.get(otherParticleEvent.getTime());
                if (otherParticleEvents != null) {
                    otherParticleEvents.remove(otherParticleEvent);
                    otherParticleEvents.remove(otherParticleEvent.getInverse());
                    if (otherParticleEvents.isEmpty()) {
                        newNextEvents.remove(otherParticleEvent.getTime());
                    }
                }
            }

            // Recalculamos proximos eventos de las particulas colisionadas
            Event newEvent = this.getEvent(event.getParticle(), absoluteTime);
            if (newEvent != null) {
                newParticleNextEvent.put(newEvent.getParticle(), newEvent);
                newNextEvents.computeIfAbsent(newEvent.getTime(), time -> new HashSet<>()).add(newEvent);
            }
            if (!event.collidesWithWall()) {
                Event newOtherEvent = this.getEvent(event.getOtherParticle(), absoluteTime);
                if (newOtherEvent != null && !newOtherEvent.equalsInverse(newEvent)) {
                    newParticleNextEvent.put(newOtherEvent.getParticle(), newOtherEvent);
                    newNextEvents.computeIfAbsent(newOtherEvent.getTime(), time -> new HashSet<>()).add(newOtherEvent);
                }
            }
        }

        return new Step(newNextEvents, newParticleNextEvent, absoluteTime - previousStep.getAbsoluteTime(), absoluteTime, previousStep.getStep() + 1);
    }

    private Step calculateFirstStep() {
        TreeMap<Double, Set<Event>> nextEvents = new TreeMap<>();
        Map<Particle, Event> particleNextEvent = new HashMap<>();

        // Put eventos
        for (Particle p : this.particles) {
            Event event = this.getEvent(p, 0);
            if (event == null)
                continue;

            if (!event.collidesWithWall()) {
                Set<Event> setEvents = nextEvents.get(event.getTime());
                if (setEvents != null) {
                    if (!setEvents.contains(event.getInverse())) {
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

        return new Step(nextEvents, particleNextEvent, 0, 0, 0);
    }

    private Event getEvent(Particle particle, double absoluteTime) {
        Pair<Double, Direction> wallCollision = Equations.getInstance().collisionWall(particle, this.dimen);
        Pair<Double, Particle> particleCollision = Equations.getInstance().collisionParticles(particle, this.particles);

        if (wallCollision.getKey() == Double.POSITIVE_INFINITY && particleCollision.getKey() == Double.POSITIVE_INFINITY)
            return null;

        if (wallCollision.getKey() < particleCollision.getKey()) {
            return new Event(wallCollision.getKey() + absoluteTime, particle, wallCollision.getValue());
        } else {
            return new Event(particleCollision.getKey() + absoluteTime, particle, particleCollision.getValue());
        }
    }

    private void serializeSystem() {
        for (Serializer serializer : this.serializers) {
            serializer.serializeSystem(this.particles, this.dimen);
        }
    }

    private void serialize(int step, double dt, double absoluteTime) {
        for (Serializer serializer : this.serializers) {
            serializer.serialize(this.particles, step, dt, absoluteTime);
        }
    }

    private static class Step {
        private final TreeMap<Double, Set<Event>> nextEvents;
        private final Map<Particle, Event> particleNextEvent;
        private final double deltaTime;
        private final double absoluteTime;
        private final int step;

        public Step(TreeMap<Double, Set<Event>> nextEvents, Map<Particle, Event> particleNextEvent, double deltaTime, double absoluteTime, int step) {
            this.nextEvents = nextEvents;
            this.particleNextEvent = particleNextEvent;
            this.deltaTime = deltaTime;
            this.absoluteTime = absoluteTime;
            this.step = step;
        }

        public TreeMap<Double, Set<Event>> getNextEvents() {
            return this.nextEvents;
        }

        public Map<Particle, Event> getParticleNextEvent() {
            return this.particleNextEvent;
        }

        public double getRelativeTime() {
            return this.deltaTime;
        }

        public double getAbsoluteTime() {
            return this.absoluteTime;
        }

        public int getStep() {
            return this.step;
        }
    }
}
