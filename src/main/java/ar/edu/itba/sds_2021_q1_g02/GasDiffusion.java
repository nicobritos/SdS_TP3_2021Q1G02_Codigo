package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.serializer.Serializer;
import javafx.util.Pair;

import java.util.*;

public class GasDiffusion {
    private final Configuration configuration;
    private final List<Particle> particles;
    private final List<Serializer> serializers;

    public GasDiffusion(List<Particle> particles, Configuration configuration) {
        this.particles = particles;
        this.configuration = configuration;
        this.serializers = new LinkedList<>();
    }

    public void addSerializer(Serializer serializer) {
        this.serializers.add(serializer);
    }

    public void simulate(int maxIterations) {
        this.serializeSystem();
        Step step = this.calculateFirstStep();
        this.serialize(step);

        while (step.getStep() < maxIterations && !this.halfOccupationFactor(step)) {
            step = this.simulateStep(step);
            if (step == null)
                return;

            this.serialize(step);
        }
    }

    private Step simulateStep(Step previousStep) {
        Map.Entry<Double, Set<Event>> firstEvents = previousStep.getFirstEvents();
        if (firstEvents == null)
            return null;
        double absoluteTime = firstEvents.getKey();

        // Mover particulas a primer proximo evento
        Equations.getInstance().evolveParticlesPositions(this.particles, absoluteTime - previousStep.getAbsoluteTime());

        TreeMap<Double, Set<Event>> newNextEvents = new TreeMap<>(previousStep.getNextEvents());

        int particlesOnLeftSide = previousStep.getParticlesOnLeftSide();
        for (Event event : firstEvents.getValue()) {
            if (!event.isValid())
                continue;

            // Colisionar
            event.getParticle().increaseCollision();
            if (event.getEventType().equals(EventType.COLLISION_WITH_WALL)) {
                Velocity newVelocity = Equations.getInstance().evolveParticleVelocity(event.getParticle(),
                        ((CollisionWithWallEvent) event).getWallDirection());
                event.getParticle().setVelocity(newVelocity);
            } else if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                Pair<Velocity, Velocity> velocities =
                        Equations.getInstance().evolveParticlesVelocities(event.getParticle(),
                                ((CollisionWithParticleEvent) event).getOtherParticle());
                event.getParticle().setVelocity(velocities.getKey());
                ((CollisionWithParticleEvent) event).getOtherParticle().setVelocity(velocities.getValue());
                ((CollisionWithParticleEvent) event).getOtherParticle().increaseCollision();
            } else {
                MovementTowards movementTowards = ((GoThroughApertureEvent) event).getMovementTowards();
                particlesOnLeftSide += (movementTowards.equals(MovementTowards.RIGHT) ? -1 : 1);
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
            if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                newNextEvent.remove(((CollisionWithParticleEvent) event).getInverse());
            }
            if (newNextEvent.isEmpty())
                newNextEvents.remove(event.getTime());

            // Recalculamos proximos eventos de las particulas colisionadas
            Set<Event> newEvents = this.getEvent(event.getParticle(), absoluteTime, previousStep.getStep());
            for (Event newEvent : newEvents) {
                newNextEvents.computeIfAbsent(newEvent.getTime(), time -> new HashSet<>()).add(newEvent);
                Set<Event> events = newNextEvents.computeIfAbsent(newEvent.getTime(), time -> new HashSet<>());
                // Lo sacamos por si ya existe. Como hashcode no contempla el contador de colisiones
                // entonces no se va a agregar un evento valido
                events.remove(newEvent);
                events.add(newEvent);
            }

            if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                Set<Event> newOtherEvents = this.getEvent(((CollisionWithParticleEvent) event).getOtherParticle(),
                        absoluteTime, previousStep.getStep());
                for (Event newOtherEvent : newOtherEvents) {
//                    if (newOtherEvent.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                    Event newOtherEventInverse =
                            newOtherEvent.getEventType().equals(EventType.COLLISION_WITH_PARTICLE) ?
                                    ((CollisionWithParticleEvent) newOtherEvent).getInverse() : newOtherEvent;
                    if (newEvents.contains(newOtherEventInverse))
                        continue;

                    if (!newEvents.contains(newOtherEvent)) {
                        Set<Event> events = newNextEvents.computeIfAbsent(newOtherEvent.getTime(),
                                time -> new HashSet<>());
                        // Lo sacamos por si ya existe. Como hashcode no contempla el contador de colisiones
                        // entonces no se va a agregar un evento valido
                        events.remove(newOtherEvent);
                        events.add(newOtherEvent);
                    }
                }
            }
        }

        return new Step(newNextEvents, absoluteTime - previousStep.getAbsoluteTime(), absoluteTime,
                previousStep.getStep() + 1, this.particles.size(), particlesOnLeftSide);
    }

    private Step calculateFirstStep() {
        TreeMap<Double, Set<Event>> nextEvents = new TreeMap<>();

        // Put eventos
        for (Particle p : this.particles) {
            Set<Event> events = this.getEvent(p, 0, 0);
            if (events.isEmpty())
                continue;

            for (Event event : events) {
                nextEvents.computeIfAbsent(event.getTime(), time -> new HashSet<>()).add(event);
            }
        }

        return new Step(nextEvents, 0, 0, 0, this.particles.size(), this.particles.size());
    }

    private Set<Event> getEvent(Particle particle, double absoluteTime, int step) {
        Pair<Double, Direction> wallCollision = Equations.getInstance().collisionWall(particle, this.configuration.getDimen());
        Pair<Double, Particle> particleCollision = Equations.getInstance().collisionParticles(particle, this.particles);
        Pair<Double, MovementTowards> goThroughAperture = Equations.getInstance().goThroughApertureTime(particle, this.configuration.getDimen());

        Set<Event> events = new HashSet<>();
        if (wallCollision.getKey() != Double.POSITIVE_INFINITY)
            events.add(new CollisionWithWallEvent(wallCollision.getKey() + absoluteTime, particle,
                    wallCollision.getValue()));
        if (particleCollision.getKey() != Double.POSITIVE_INFINITY)
            events.add(new CollisionWithParticleEvent(particleCollision.getKey() + absoluteTime, particle,
                    particleCollision.getValue()));
        if (goThroughAperture.getKey() != Double.POSITIVE_INFINITY)
            events.add(new GoThroughApertureEvent(goThroughAperture.getKey() + absoluteTime, particle,
                    goThroughAperture.getValue()));

        return events;
    }

    private void serializeSystem() {
        for (Serializer serializer : this.serializers) {
            serializer.serializeSystem(this.particles, this.configuration);
        }
    }

    private void serialize(Step step) {
        for (Serializer serializer : this.serializers) {
            serializer.serialize(this.particles, step);
        }
    }

    private boolean halfOccupationFactor(Step step) {
        return Math.abs(step.getLeftOccupationFactor() - 0.5) < this.configuration.getOccupationFactor();
    }
}
