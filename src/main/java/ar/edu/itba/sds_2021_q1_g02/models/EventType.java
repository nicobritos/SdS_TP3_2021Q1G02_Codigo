package ar.edu.itba.sds_2021_q1_g02.models;

public enum EventType {
    COLLISION_WITH_WALL(true),
    COLLISION_WITH_PARTICLE(true),
    GO_THROUGH_APERTURE(false);

    private final boolean priority;

    EventType(boolean priority) {
        this.priority = priority;
    }

    public boolean isPriority() {
        return this.priority;
    }
}
