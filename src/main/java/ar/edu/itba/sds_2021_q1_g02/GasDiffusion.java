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
        Map.Entry<Double, Set<Event>> firstEvents = previousStep.getFirstEvents();
        double absoluteTime = firstEvents.getKey();

        // Mover particulas a primer proximo evento
        Equations.getInstance().evolveParticlesPositions(this.particles, absoluteTime - previousStep.getAbsoluteTime());

        TreeMap<Double, Set<Event>> newNextEvents = new TreeMap<>(previousStep.getNextEvents());

        for (Event event : firstEvents.getValue()) {
            // Colisionar
            event.getParticle().increaseCollision();
            if (event.collidesWithWall()) {
                Velocity newVelocity = Equations.getInstance().evolveParticleVelocity(event.getParticle(), event.getWallDirection());
                event.getParticle().setVelocity(newVelocity);
            } else {
                Pair<Velocity, Velocity> velocities = Equations.getInstance().evolveParticlesVelocities(event.getParticle(), event.getOtherParticle());
                event.getParticle().setVelocity(velocities.getKey());
                event.getOtherParticle().setVelocity(velocities.getValue());
                event.getOtherParticle().increaseCollision();
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

            // Recalculamos proximos eventos de las particulas colisionadas
            Event newEvent = this.getEvent(event.getParticle(), absoluteTime);
            if (newEvent != null) {
                newNextEvents.computeIfAbsent(newEvent.getTime(), time -> new HashSet<>()).add(newEvent);
            }
            if (!event.collidesWithWall()) {
                Event newOtherEvent = this.getEvent(event.getOtherParticle(), absoluteTime);
                if (newOtherEvent != null && !newOtherEvent.equalsInverse(newEvent)) {
                    newNextEvents.computeIfAbsent(newOtherEvent.getTime(), time -> new HashSet<>()).add(newOtherEvent);
                }
            }
        }

        return new Step(newNextEvents, absoluteTime - previousStep.getAbsoluteTime(), absoluteTime, previousStep.getStep() + 1);
    }

    private Step calculateFirstStep() {
        TreeMap<Double, Set<Event>> nextEvents = new TreeMap<>();

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
        }

        return new Step(nextEvents, 0, 0, 0);
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
        private final double deltaTime;
        private final double absoluteTime;
        private final int step;

        public Step(TreeMap<Double, Set<Event>> nextEvents, double deltaTime, double absoluteTime, int step) {
            this.nextEvents = nextEvents;
            this.deltaTime = deltaTime;
            this.absoluteTime = absoluteTime;
            this.step = step;
        }

        public TreeMap<Double, Set<Event>> getNextEvents() {
            return this.nextEvents;
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

        public Map.Entry<Double, Set<Event>> getFirstEvents() {
            Map.Entry<Double, Set<Event>> firstEvents;

            do {
                firstEvents = this.nextEvents.firstEntry();
                if (firstEvents == null)
                    return null;

                this.removeStaleEvents(firstEvents.getValue());
                if (firstEvents.getValue().isEmpty()) {
                    this.nextEvents.remove(firstEvents.getKey());
                }
            } while (firstEvents.getValue().isEmpty());

            return firstEvents;
        }

        private void removeStaleEvents(Set<Event> events) {
            events.removeIf(event -> !event.isValid());
        }
    }
}
