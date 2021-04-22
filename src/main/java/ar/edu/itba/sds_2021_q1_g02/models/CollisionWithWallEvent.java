package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Objects;

public class CollisionWithWallEvent extends Event {
    private final Direction wallDirection;
    private final EventType eventType;

    public CollisionWithWallEvent(double time, Particle particle, Direction wallDirection) {
        super(time, particle);

        this.wallDirection = wallDirection;
        this.eventType = EventType.COLLISION_WITH_WALL;
    }

    public Direction getWallDirection() {
        return this.wallDirection;
    }

    @Override
    public void setCollided() {
        super.setCollided();

        Velocity newVelocity = Equations.getInstance().evolveParticleVelocity(this.particle, this.wallDirection);
        this.particle.setVelocity(newVelocity);
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.time, this.particle, this.wallDirection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollisionWithWallEvent)) return false;
        CollisionWithWallEvent event = (CollisionWithWallEvent) o;
        return Double.compare(event.getTime(), this.time) == 0 && this.particle.equals(event.getParticle()) &&
                this.wallDirection == event.getWallDirection();
    }
}
