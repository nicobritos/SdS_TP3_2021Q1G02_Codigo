package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Objects;

public class CollisionWithParticleEvent implements IEvent, Comparable<CollisionWithWallEvent> {
    private final double time;
    private final Particle particle;
    private final Particle otherParticle;
    private final int collisionCountParticle1;
    private final int collisionCountParticle2;
    private final EventType eventType;

    public CollisionWithParticleEvent(double time, Particle particle, Particle otherParticle) {
        this.time = time;

        this.particle = particle;
        this.otherParticle = otherParticle;
        this.collisionCountParticle1 = this.particle.getCollisionCount();
        this.collisionCountParticle2 = this.otherParticle.getCollisionCount();
        this.eventType = EventType.COLLISION_WITH_PARTICLE;
    }

    public boolean isValid() {
        return this.collisionCountParticle1 == this.particle.getCollisionCount() && this.collisionCountParticle2 == this.otherParticle.getCollisionCount();
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    public CollisionWithParticleEvent getInverse() {
        return new CollisionWithParticleEvent(this.time, this.otherParticle, this.particle);
    }

    public Particle getOtherParticle() {
        return this.otherParticle;
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
        return Objects.hash(this.getTime(), this.getParticle(), this.getOtherParticle());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollisionWithParticleEvent)) return false;
        CollisionWithParticleEvent event = (CollisionWithParticleEvent) o;
        return Double.compare(event.getTime(), this.getTime()) == 0 && this.getParticle().equals(event.getParticle()) && Objects.equals(this.getOtherParticle(), event.getOtherParticle());
    }

    @Override
    public int compareTo(CollisionWithWallEvent o) {
        return Double.compare(this.time, o.getTime());
    }
}
