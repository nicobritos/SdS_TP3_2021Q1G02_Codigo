package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Objects;

public class Event implements Comparable<Event> {
    private final double time;
    private final Particle particle;
    private final Particle otherParticle;
    private final Direction wallDirection;
    private final int collisionCountParticle1;
    private final int collisionCountParticle2;

    public Event(double time, Particle particle, Particle otherParticle) {
        this.time = time;

        this.particle = particle;
        this.otherParticle = otherParticle;
        this.collisionCountParticle1 = this.particle.getCollisionCount();
        this.collisionCountParticle2 = this.otherParticle.getCollisionCount();

        this.wallDirection = null;
    }

    public Event(double time, Particle particle, Direction wallDirection) {
        this.time = time;

        this.particle = particle;
        this.wallDirection = wallDirection;
        this.collisionCountParticle1 = this.particle.getCollisionCount();
        this.collisionCountParticle2 = 0;

        this.otherParticle = null;
    }

    public double getTime() {
        return this.time;
    }

    public Particle getParticle() {
        return this.particle;
    }

    public Particle getOtherParticle() {
        return this.otherParticle;
    }

    public Direction getWallDirection() {
        return this.wallDirection;
    }

    public boolean collidesWithWall() {
        return this.wallDirection != null;
    }

    public boolean isValid() {
        return this.collisionCountParticle1 == this.particle.getCollisionCount() &&
                (this.collidesWithWall() || this.collisionCountParticle2 == this.otherParticle.getCollisionCount());
    }

    @Override
    public int compareTo(Event o) {
        return Double.compare(this.time, o.time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Double.compare(event.getTime(), this.getTime()) == 0 && this.getParticle().equals(event.getParticle()) && Objects.equals(this.getOtherParticle(), event.getOtherParticle()) && this.getWallDirection() == event.getWallDirection();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getTime(), this.getParticle(), this.getOtherParticle(), this.getWallDirection());
    }

    public boolean equalsInverse(Event other) {
        if (this.equals(other))
            return true;

        if (this.collidesWithWall() != other.collidesWithWall())
            return false;

        return (this.time == other.time &&
                this.collidesWithWall() ||
                (this.getParticle().equals(other.getOtherParticle()) && this.getOtherParticle().equals(this.getParticle()))
        );
    }

    public Event getInverse() {
        if (this.collidesWithWall())
            return new Event(this.time, this.particle, this.wallDirection);
        return new Event(this.time, this.otherParticle, this.particle);
    }
}
