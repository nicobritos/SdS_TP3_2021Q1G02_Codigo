package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.util.Objects;

public class CollisionWithParticleEvent extends Event {
    private final Particle otherParticle;
    private final int collisionCountParticle2;
    private final EventType eventType;

    public CollisionWithParticleEvent(double time, Particle particle, Particle otherParticle) {
        super(time, particle);

        this.otherParticle = otherParticle;
        this.collisionCountParticle2 = this.otherParticle.getCollisionCount();
        this.eventType = EventType.COLLISION_WITH_PARTICLE;
    }

    public CollisionWithParticleEvent getInverse() {
        return new CollisionWithParticleEvent(this.time, this.otherParticle, this.particle);
    }

    public Particle getOtherParticle() {
        return this.otherParticle;
    }

    public int getCollisionCountParticle2() {
        return this.collisionCountParticle2;
    }

    @Override
    public void setCollided() {
        super.setCollided();

        Pair<Velocity, Velocity> velocities = Equations.getInstance().evolveParticlesVelocities(this.particle, this.otherParticle);
        this.particle.setVelocity(velocities.getKey());
        this.otherParticle.setVelocity(velocities.getValue());
        this.otherParticle.increaseCollision();
    }

    @Override
    public boolean isValid() {
        return this.collisionCountParticle1 == this.particle.getCollisionCount() && this.collisionCountParticle2 == this.otherParticle.getCollisionCount();
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.time, this.particle, this.otherParticle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollisionWithParticleEvent)) return false;
        CollisionWithParticleEvent event = (CollisionWithParticleEvent) o;
        return Double.compare(event.getTime(), this.time) == 0 && this.particle.equals(event.getParticle()) && Objects.equals(this.otherParticle, event.getOtherParticle());
    }
}
