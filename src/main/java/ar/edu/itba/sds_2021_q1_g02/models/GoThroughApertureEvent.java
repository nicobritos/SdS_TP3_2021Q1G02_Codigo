package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Objects;

public class GoThroughApertureEvent implements IEvent, Comparable<CollisionWithWallEvent> {
    private final double time;
    private final Particle particle;
    private final EventType eventType;

    public GoThroughApertureEvent(double time, Particle particle) {
        this.time = time;

        this.particle = particle;
        this.eventType = EventType.GO_THROUGH_APERTURE;
    }

    public boolean isValid() {
        return true;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public double getTime() {
        return this.time;
    }

    @Override
    public Particle getParticle() {
        return this.particle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getTime(), this.getParticle());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoThroughApertureEvent)) return false;
        GoThroughApertureEvent event = (GoThroughApertureEvent) o;
        return Double.compare(event.getTime(), this.getTime()) == 0 && this.getParticle().equals(event.getParticle());
    }

    @Override
    public int compareTo(CollisionWithWallEvent o) {
        return Double.compare(this.time, o.getTime());
    }
}
