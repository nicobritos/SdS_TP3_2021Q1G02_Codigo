package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.serializer.Serializer;
import javafx.util.Pair;

import java.util.*;

public class GasDiffusion {
    private final Configuration configuration;
    private final List<Particle> particles;
    private final List<Serializer> serializers;
    private Double systemPressure = Double.POSITIVE_INFINITY;
    private int pressureParticlesCollided = 0;

    public GasDiffusion(List<Particle> particles, Configuration configuration) {
        this.particles = particles;
        this.configuration = configuration;
        this.serializers = new LinkedList<>();
    }

    public void addSerializer(Serializer serializer) {
        this.serializers.add(serializer);
    }

    public void simulate() {
        this.serializeSystem();
        Step step = this.calculateFirstStep();
        this.serialize(step);

        while (!this.halfOccupationFactor(step)) {
            step = this.simulateStep(step, false);
            if (step == null)
                return;

            this.serialize(step);
        }

        if (this.halfOccupationFactor(step)) {
            double t = step.getAbsoluteTime() + this.configuration.getDt();
            while (step.getAbsoluteTime() < t) {
                step = this.simulateStep(step, true);
                if (step == null)
                    return;
            }
        }

        System.out.println(this.pressureParticlesCollided);
        System.out.println(this.systemPressure);
    }

    private Step simulateStep(Step previousStep, boolean inEquilibrium) {
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

        if (inEquilibrium) {
            for (Event event : firstEvents.getValue()) {
                if (event.getParticle().getPosition().getX() < this.configuration.getDimen().getApertureX()) {
                    if (event.getEventType().equals(EventType.COLLISION_WITH_WALL)) {
                        if (((CollisionWithWallEvent) event).getWallDirection().equals(Direction.VERTICAL) && event.getParticle().getVelocity().getxSpeed() > 0) {
                            // CHOQUE CONTRA LA PARED VERTICAL EN X=0
                            double finalSpeed = event.getParticle().getVelocity().getxSpeed();
                            double initSpeed = - finalSpeed;
                            double d = this.configuration.getDimen().getYvf() - this.configuration.getDimen().getYvi();
                            if (this.systemPressure == Double.POSITIVE_INFINITY) this.systemPressure = 0.0;
                            this.systemPressure += Pressure.calculate(initSpeed, finalSpeed,
                                    event.getParticle().getMass(), this.configuration.getDt(), d);
                            this.pressureParticlesCollided += 1;
                        }
                    }
                }
            }
        }


        // Copiamos el mapa y no iteramos sobre el mismo set para no
        // eliminar el mismo evento mientras se itera
        for (Map.Entry<Double, EventCollection> entry : previousStep.getNextEvents().entrySet()) {
            newNextEvents.put(entry.getKey(), new EventCollection(new HashSet<>(entry.getValue().getPriorityEvents())
                    , new HashSet<>(entry.getValue().getOtherEvents())));
        }

        EventCollection newNextEvent = newNextEvents.get(firstEvents.getKey());
        for (Event event : firstEvents.getValue()) {
            this.postProcessEvent(newNextEvents, previousStep.getParticleEvents(), newNextEvent, event, absoluteTime);
        }

        return new Step(newNextEvents, previousStep.getParticleEvents(),
                absoluteTime - previousStep.getAbsoluteTime(), absoluteTime,
                previousStep.getStep() + 1, this.particles.size(), particlesOnLeftSide);
    }

    private Step calculateFirstStep() {
        TreeMap<Double, EventCollection> nextEvents = new TreeMap<>();
        Map<Particle, Set<Event>> particleEvents = new HashMap<>();

        // Put eventos
        for (Particle p : this.particles) {
            EventCollection eventCollection = this.getEvent(p, 0);
            if (eventCollection.isEmpty())
                continue;

            for (Event event : eventCollection) {
                nextEvents.computeIfAbsent(event.getTime(), time -> new EventCollection()).add(event);
                if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                    CollisionWithParticleEvent collisionEvent = (CollisionWithParticleEvent) event;

                    particleEvents.computeIfAbsent(collisionEvent.getOtherParticle(), particle -> new HashSet<>()).add(collisionEvent);
                }
            }
        }

        return new Step(nextEvents, particleEvents, 0, 0, 0, this.particles.size(), this.particles.size());
    }

    private void postProcessEvent(Map<Double, EventCollection> eventMap, Map<Particle, Set<Event>> particleMap,
                                  EventCollection events, Event event, double absoluteTime) {
        // El evento ya paso
        events.remove(event);
        if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
            CollisionWithParticleEvent collisionEvent = (CollisionWithParticleEvent) event;

            events.remove(collisionEvent.getInverse());
            // Tenemos que recalcular todos los eventos en donde las particulas que participaron
            // en la colision participen como "otherParticle"
            this.processVoidedEvents(eventMap, particleMap, collisionEvent.getOtherParticle(), absoluteTime);
        }
        if (events.isEmpty())
            eventMap.remove(event.getTime());
        if (event.hasCollided()) {
            // Tenemos que recalcular todos los eventos en donde las particulas que participaron
            // en la colision participen como "particle"
            this.processVoidedEvents(eventMap, particleMap, event.getParticle(), absoluteTime);
        }

        // Recalculamos proximos eventos de las particulas colisionadas
        EventCollection newEvents = this.getEvent(event.getParticle(), absoluteTime);
        for (Event newEvent : newEvents) {
            this.insertEventInMap(eventMap, particleMap, newEvent);
        }

        if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
            EventCollection newOtherEvents = this.getEvent(((CollisionWithParticleEvent) event).getOtherParticle(),
                    absoluteTime);

            for (Event newOtherEvent : newOtherEvents) {
                if (newOtherEvent.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
                    Event newOtherEventInverse = ((CollisionWithParticleEvent) newOtherEvent).getInverse();
                    if (newEvents.contains(newOtherEventInverse))
                        continue;
                }

                if (!newEvents.contains(newOtherEvent)) {
                    this.insertEventInMap(eventMap, particleMap, newOtherEvent);
                }
            }
        }
    }

    private void processVoidedEvents(Map<Double, EventCollection> eventMap, Map<Particle, Set<Event>> particleMap,
                                     Particle particle, double absoluteTime) {
        Set<Event> voidedEvents = particleMap.get(particle);
        if (voidedEvents != null) {
            particleMap.remove(particle);

            for (Event event : voidedEvents) {
                for (Event newEvent : this.getEvent(event.getParticle(), absoluteTime)) {
                    this.insertEventInMap(eventMap, particleMap, newEvent);
                }
            }
        }
    }

    private void insertEventInMap(Map<Double, EventCollection> eventMap, Map<Particle, Set<Event>> particleMap,
                                  Event event) {
        EventCollection eventCollection = eventMap.computeIfAbsent(event.getTime(), time -> new EventCollection());

        // Lo sacamos por si ya existe. Como hashcode no contempla el contador de colisiones
        // entonces no se va a agregar un evento valido
        eventCollection.remove(event);
        eventCollection.add(event);

        if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE)) {
            CollisionWithParticleEvent collisionEvent = (CollisionWithParticleEvent) event;
            Set<Event> otherParticleEvents = particleMap.computeIfAbsent(collisionEvent.getOtherParticle(),
                    time -> new HashSet<>());

            otherParticleEvents.remove(event);
            otherParticleEvents.add(event);
        }
    }

    /**
     * Returns how much particles left or returned to the left enclosure
     *
     * @param event
     * @return
     */
    private int processEvent(Event event) {
        if (!event.isValid())
            return 0;

        // Colisionar
        event.setCollided();
        if (event.getEventType().equals(EventType.GO_THROUGH_APERTURE)) {
            MovementTowards movementTowards = ((GoThroughApertureEvent) event).getMovementTowards();
            return movementTowards.equals(MovementTowards.RIGHT) ? -1 : 1;
        }

        return 0;
    }

    private EventCollection getEvent(Particle particle, double absoluteTime) {
        Pair<Double, Direction> wallCollision = Equations.getInstance().collisionWall(particle,
                this.configuration.getDimen());
        Pair<Double, Particle> particleCollision = Equations.getInstance().collisionParticles(particle, this.particles);
        Pair<Double, MovementTowards> goThroughAperture = Equations.getInstance().goThroughApertureTime(particle,
                this.configuration.getDimen());

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
