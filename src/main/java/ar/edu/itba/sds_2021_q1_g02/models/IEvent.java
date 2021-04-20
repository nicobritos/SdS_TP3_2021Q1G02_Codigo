package ar.edu.itba.sds_2021_q1_g02.models;

public interface IEvent {
    double getTime();

    Particle getParticle();

    boolean isValid();

    EventType getEventType();

}
