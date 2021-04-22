package ar.edu.itba.sds_2021_q1_g02.models;

public abstract class Event implements Comparable<Event> {
    protected final double time;
    protected final Particle particle;
    protected final int collisionCountParticle1;
    protected boolean collided;

    protected Event(double time, Particle particle) {
        this.time = time;

        this.particle = particle;
        this.collisionCountParticle1 = this.particle.getCollisionCount();
        this.collided = false;
    }

    public double getTime() {
        return this.time;
    }

    public Particle getParticle() {
        return this.particle;
    }

    public boolean isValid() {
        return this.collisionCountParticle1 == this.particle.getCollisionCount();
    }

    public void setCollided() {
        this.collided = true;
        this.particle.increaseCollision();
    }

    public boolean hasCollided() {
        return this.collided;
    }

    @Override
    public int compareTo(Event o) {
        return Double.compare(this.time, o.getTime());
    }

    public abstract EventType getEventType();
}
