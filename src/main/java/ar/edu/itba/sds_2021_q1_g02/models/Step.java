package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Step {
    private final TreeMap<Double, EventCollection> nextEvents;
    private final Map<Particle, Set<Event>> particleEvents;
    private final double deltaTime;
    private final double absoluteTime;
    private final double leftOccupationFactor;
    private final int particlesOnLeftSide;
    private final int step;

    public Step(TreeMap<Double, EventCollection> nextEvents, Map<Particle, Set<Event>> particleEvents, double deltaTime, double absoluteTime, int step, int particleCount, int particlesOnLeftSide) {
        this.nextEvents = nextEvents;
        this.particleEvents = particleEvents;
        this.deltaTime = deltaTime;
        this.absoluteTime = absoluteTime;
        this.step = step;
        this.particlesOnLeftSide = particlesOnLeftSide;

        if (particlesOnLeftSide == 0)
            this.leftOccupationFactor = 0;
        else
            this.leftOccupationFactor = particlesOnLeftSide / ((double) particleCount);
    }

    public TreeMap<Double, EventCollection> getNextEvents() {
        return this.nextEvents;
    }

    public Map<Particle, Set<Event>> getParticleEvents() {
        return this.particleEvents;
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

    public Map.Entry<Double, EventCollection> getFirstEvents() {
        Map.Entry<Double, EventCollection> firstEvents;

        do {
            firstEvents = this.nextEvents.firstEntry();
            if (firstEvents == null)
                return null;

            this.removeStaleEvents(firstEvents.getValue());
            if (firstEvents.getValue().getPriorityEvents().isEmpty() && firstEvents.getValue().getOtherEvents().isEmpty()) {
                this.nextEvents.remove(firstEvents.getKey());
            }
        } while (firstEvents.getValue().getPriorityEvents().isEmpty() && firstEvents.getValue().getOtherEvents().isEmpty());

        return firstEvents;
    }

    public double getLeftOccupationFactor() {
        return this.leftOccupationFactor;
    }

    public int getParticlesOnLeftSide() {
        return this.particlesOnLeftSide;
    }

    private void removeStaleEvents(EventCollection events) {
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (!event.isValid()) {
                iterator.remove();

                this.removeStaleParticleEvent(event.getParticle(), event);
                if (event.getEventType().equals(EventType.COLLISION_WITH_PARTICLE))
                    this.removeStaleParticleEvent(((CollisionWithParticleEvent) event).getOtherParticle(), event);
            }
        }
    }

    private void removeStaleParticleEvent(Particle particle, Event event) {
        Set<Event> collection = this.particleEvents.get(particle);
        if (collection != null) {
            collection.remove(event);
            if (collection.isEmpty())
                this.particleEvents.remove(particle);
        }
    }
}
