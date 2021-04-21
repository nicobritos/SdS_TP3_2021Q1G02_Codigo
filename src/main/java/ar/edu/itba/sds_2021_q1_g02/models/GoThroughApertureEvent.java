package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Objects;

public class GoThroughApertureEvent extends Event {
    private final EventType eventType;
    private final MovementTowards movementTowards;

    public GoThroughApertureEvent(double time, Particle particle, MovementTowards movementTowards) {
        super(time, particle);

        this.movementTowards = movementTowards;
        this.eventType = EventType.GO_THROUGH_APERTURE;
    }

    public MovementTowards getMovementTowards() {
        return this.movementTowards;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.time, this.particle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoThroughApertureEvent)) return false;
        GoThroughApertureEvent event = (GoThroughApertureEvent) o;
        return Double.compare(event.getTime(), this.time) == 0 && this.particle.equals(event.getParticle());
    }
}
