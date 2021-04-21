package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Step {
    private final TreeMap<Double, Set<Event>> nextEvents;
    private final double deltaTime;
    private final double absoluteTime;
    private final double leftOccupationFactor;
    private final int particlesOnLeftSide;
    private final int step;

    public Step(TreeMap<Double, Set<Event>> nextEvents, double deltaTime, double absoluteTime, int step, int particleCount, int particlesOnLeftSide) {
        this.nextEvents = nextEvents;
        this.deltaTime = deltaTime;
        this.absoluteTime = absoluteTime;
        this.step = step;
        this.particlesOnLeftSide = particlesOnLeftSide;

        this.leftOccupationFactor = ((double) particleCount) / particlesOnLeftSide;
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

    public double getLeftOccupationFactor() {
        return this.leftOccupationFactor;
    }

    public int getParticlesOnLeftSide() {
        return this.particlesOnLeftSide;
    }

    private void removeStaleEvents(Set<Event> events) {
        int step = this.step;
        events.removeIf(event -> {
            return !event.isValid();
        });
    }
}
