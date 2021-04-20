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
        Map.Entry<Double, Set<IEvent>> firstEvents = previousStep.getFirstEvents();
        if (firstEvents == null)
            return null; // TODO
        double absoluteTime = firstEvents.getKey();

        // Mover particulas a primer proximo evento
        Equations.getInstance().evolveParticlesPositions(this.particles, absoluteTime - previousStep.getAbsoluteTime());

        TreeMap<Double, Set<IEvent>> newNextEvents = new TreeMap<>(previousStep.getNextEvents());

        for (IEvent event : firstEvents.getValue()) {
            if (!event.isValid())
                continue;

            // Colisionar
            event.getParticle().increaseCollision();
            if (event.getEventType().equals(EventType.COLLISION_WITH_WALL)) {
                Velocity newVelocity = Equations.getInstance().evolveParticleVelocity(event.getParticle(),
                        ((CollisionWithWallEvent) event).getWallDirection());
                event.getParticle().setVelocity(newVelocity);
            } else {
                Pair<Velocity, Velocity> velocities =
                        Equations.getInstance().evolveParticlesVelocities(event.getParticle(),
                                ((CollisionWithParticleEvent) event).getOtherParticle());
                event.getParticle().setVelocity(velocities.getKey());
                ((CollisionWithParticleEvent) event).getOtherParticle().setVelocity(velocities.getValue());
                ((CollisionWithParticleEvent) event).getOtherParticle().increaseCollision();
            }
        }

        // Copiamos el mapa y no iteramos sobre el mismo set para no
        // eliminar el mismo evento mientras se itera
        for (Map.Entry<Double, Set<IEvent>> entry : previousStep.getNextEvents().entrySet()) {
            newNextEvents.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }

        Set<IEvent> newNextEvent = newNextEvents.get(firstEvents.getKey());
        for (IEvent event : firstEvents.getValue()) {
            // El evento ya paso
            newNextEvent.remove(event);
            if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                newNextEvent.remove(((CollisionWithParticleEvent) event).getInverse());
            }
            if (newNextEvent.isEmpty())
                newNextEvents.remove(event.getTime());

            // Recalculamos proximos eventos de las particulas colisionadas
            Set<IEvent> newEvents = this.getEvent(event.getParticle(), absoluteTime, previousStep.step);
            for (IEvent newEvent : newEvents) {
                newNextEvents.computeIfAbsent(newEvent.getTime(), time -> new HashSet<>()).add(newEvent);
            }

            if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                Set<IEvent> newOtherEvents = this.getEvent(((CollisionWithParticleEvent) event).getOtherParticle(),
                        absoluteTime, previousStep.step);
                for (IEvent newOtherEvent : newOtherEvents) {
//                    if (newOtherEvent.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                    IEvent newOtherEventInverse =
                            newOtherEvent.getEventType().equals(EventType.COLLISION_WITH_PARTICLE) ?
                                    ((CollisionWithParticleEvent) newOtherEvent).getInverse() : newOtherEvent;
                    if (!newEvents.contains(newOtherEventInverse)) {
                        newNextEvents.computeIfAbsent(newOtherEvent.getTime(), time -> new HashSet<>()).add(newOtherEvent);
                    }
//                    }
                }
            }
        }

        return new Step(newNextEvents, absoluteTime - previousStep.getAbsoluteTime(), absoluteTime,
                previousStep.getStep() + 1);
    }

    private Step calculateFirstStep() {
        TreeMap<Double, Set<IEvent>> nextEvents = new TreeMap<>();

        // Put eventos
        for (Particle p : this.particles) {
            Set<IEvent> events = this.getEvent(p, 0, 0);
            if (events.isEmpty())
                continue;

            for (IEvent event : events) {
                nextEvents.computeIfAbsent(event.getTime(), time -> new HashSet<>()).add(event);
            }
        }

        return new Step(nextEvents, 0, 0, 0);
    }

    private Set<IEvent> getEvent(Particle particle, double absoluteTime, int step) {
        Pair<Double, Direction> wallCollision = Equations.getInstance().collisionWall(particle, this.dimen);
        Pair<Double, Particle> particleCollision = Equations.getInstance().collisionParticles(particle, this.particles);

        Set<IEvent> events = new HashSet<>();
        if (wallCollision.getKey() != Double.POSITIVE_INFINITY)
            events.add(new CollisionWithWallEvent(wallCollision.getKey() + absoluteTime, particle,
                    wallCollision.getValue()));
        if (particleCollision.getKey() != Double.POSITIVE_INFINITY)
            events.add(new CollisionWithParticleEvent(particleCollision.getKey() + absoluteTime, particle,
                    particleCollision.getValue()));

        return events;
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
        private final TreeMap<Double, Set<IEvent>> nextEvents;
        private final double deltaTime;
        private final double absoluteTime;
        private final int step;

        public Step(TreeMap<Double, Set<IEvent>> nextEvents, double deltaTime, double absoluteTime, int step) {
            this.nextEvents = nextEvents;
            this.deltaTime = deltaTime;
            this.absoluteTime = absoluteTime;
            this.step = step;
        }

        public TreeMap<Double, Set<IEvent>> getNextEvents() {
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

        public Map.Entry<Double, Set<IEvent>> getFirstEvents() {
            Map.Entry<Double, Set<IEvent>> firstEvents;

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

        private void removeStaleEvents(Set<IEvent> events) {
            int step = this.step;
            events.removeIf(event -> {
                return !event.isValid();
            });
        }
    }
}
