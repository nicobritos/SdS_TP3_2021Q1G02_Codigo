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
        Map.Entry<Double, EventCollection> firstEvents = previousStep.getFirstEvents();
        if (firstEvents == null)
            return null;
        double absoluteTime = firstEvents.getKey();

        // Mover particulas a primer proximo evento
        Equations.getInstance().evolveParticlesPositions(this.particles, absoluteTime - previousStep.getAbsoluteTime());

        TreeMap<Double, EventCollection> newNextEvents = new TreeMap<>(previousStep.getNextEvents());

        int particlesOnLeftSide = previousStep.getParticlesOnLeftSide();
        for (Event event : firstEvents.getValue()) {
            particlesOnLeftSide += this.processEvent(event);
        }

        // Copiamos el mapa y no iteramos sobre el mismo set para no
        // eliminar el mismo evento mientras se itera
        for (Map.Entry<Double, EventCollection> entry : previousStep.getNextEvents().entrySet()) {
            newNextEvents.put(entry.getKey(), new EventCollection(new HashSet<>(entry.getValue().getPriorityEvents()), new HashSet<>(entry.getValue().getOtherEvents())));
        }

        EventCollection newNextEvent = newNextEvents.get(firstEvents.getKey());
        for (Event event : firstEvents.getValue()) {
            this.postProcessEvent(newNextEvents, newNextEvent, event, absoluteTime, previousStep.getStep());
        }

        return new Step(newNextEvents, absoluteTime - previousStep.getAbsoluteTime(), absoluteTime,
                previousStep.getStep() + 1, this.particles.size(), particlesOnLeftSide);
    }

    private Step calculateFirstStep() {
        TreeMap<Double, EventCollection> nextEvents = new TreeMap<>();

        // Put eventos
        for (Particle p : this.particles) {
            EventCollection eventCollection = this.getEvent(p, 0, 0);
            if (eventCollection.isEmpty())
                continue;

            for (Event event : eventCollection) {
                nextEvents.computeIfAbsent(event.getTime(), time -> new EventCollection()).add(event);
            }
        }

        return new Step(nextEvents, 0, 0, 0, this.particles.size(), this.particles.size());
    }

    private void postProcessEvent(Map<Double, EventCollection> eventMap, EventCollection events, Event event, double absoluteTime, int step) {
        // El evento ya paso
        events.remove(event);
        if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
            events.remove(((CollisionWithParticleEvent) event).getInverse());
        }
        if (events.isEmpty())
            eventMap.remove(event.getTime());

        // Recalculamos proximos eventos de las particulas colisionadas
        EventCollection newEvents = this.getEvent(event.getParticle(), absoluteTime, step);
        for (Event newEvent : newEvents) {
            this.insertEventInMap(eventMap, newEvent);
        }

        if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
            EventCollection newOtherEvents = this.getEvent(((CollisionWithParticleEvent) event).getOtherParticle(), absoluteTime, step);

            for (Event newOtherEvent : newOtherEvents) {
                if (newOtherEvent.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                    Event newOtherEventInverse = ((CollisionWithParticleEvent) newOtherEvent).getInverse();
                    if (newEvents.contains(newOtherEventInverse))
                        continue;
                }

                if (!newEvents.contains(newOtherEvent)) {
                    this.insertEventInMap(eventMap, newOtherEvent);
                }
            }
        }
    }

    private void insertEventInMap(Map<Double, EventCollection> eventMap, Event event) {
        EventCollection eventCollection = eventMap.computeIfAbsent(event.getTime(), time -> new EventCollection());

        // Lo sacamos por si ya existe. Como hashcode no contempla el contador de colisiones
        // entonces no se va a agregar un evento valido
        eventCollection.remove(event);
        eventCollection.add(event);
    }

    /**
     * Returns how much particles left or returned to the left enclosure
     * @param event
     * @return
     */
    private int processEvent(Event event) {
        if (!event.isValid())
            return 0;

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
            return movementTowards.equals(MovementTowards.RIGHT) ? -1 : 1;
        }

        return 0;
    }

    private EventCollection getEvent(Particle particle, double absoluteTime, int step) {
        Pair<Double, Direction> wallCollision = Equations.getInstance().collisionWall(particle, this.configuration.getDimen());
        Pair<Double, Particle> particleCollision = Equations.getInstance().collisionParticles(particle, this.particles);
        Pair<Double, MovementTowards> goThroughAperture = Equations.getInstance().goThroughApertureTime(particle, this.configuration.getDimen());

        EventCollection eventCollection = new EventCollection();
        if (wallCollision.getKey() != Double.POSITIVE_INFINITY)
            eventCollection.add(new CollisionWithWallEvent(wallCollision.getKey() + absoluteTime, particle,
                    wallCollision.getValue()));
        if (particleCollision.getKey() != Double.POSITIVE_INFINITY)
            eventCollection.add(new CollisionWithParticleEvent(particleCollision.getKey() + absoluteTime, particle,
                    particleCollision.getValue()));
        if (goThroughAperture.getKey() != Double.POSITIVE_INFINITY)
            eventCollection.add(new GoThroughApertureEvent(goThroughAperture.getKey() + absoluteTime, particle,
                    goThroughAperture.getValue()));

        return eventCollection;
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
