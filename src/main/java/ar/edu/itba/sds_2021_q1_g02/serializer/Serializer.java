package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.util.Collection;
import java.util.List;

public interface Serializer {
    default void serializeSystem(Collection<Particle> particles, Dimen systemDimen) {}

    void serialize(Collection<Particle> particles, int step, double dt, double absoluteTime);
}
