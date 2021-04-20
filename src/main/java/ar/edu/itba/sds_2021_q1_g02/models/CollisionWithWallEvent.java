package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Objects;

public class CollisionWithWallEvent implements IEvent, Comparable<CollisionWithWallEvent> {
    private final double time;
    private final Particle particle;
    private final Direction wallDirection;
    private final int collisionCountParticle1;
    private final EventType eventType;

    public CollisionWithWallEvent(double time, Particle particle, Direction wallDirection) {
        this.time = time;

        this.particle = particle;
        this.wallDirection = wallDirection;
        this.collisionCountParticle1 = this.particle.getCollisionCount();
        this.eventType = EventType.COLLISION_WITH_WALL;
    }

    public boolean isValid() {
        return this.collisionCountParticle1 == this.particle.getCollisionCount();
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    public Direction getWallDirection() {
        return this.wallDirection;
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
    public int compareTo(CollisionWithWallEvent o) {
        return Double.compare(this.time, o.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getTime(), this.getParticle(), this.getWallDirection());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollisionWithWallEvent)) return false;
        CollisionWithWallEvent event = (CollisionWithWallEvent) o;
        return Double.compare(event.getTime(), this.getTime()) == 0 && this.getParticle().equals(event.getParticle()) &&
                this.getWallDirection() == event.getWallDirection();
    }
}
