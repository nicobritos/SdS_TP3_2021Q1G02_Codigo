package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.util.List;

public interface Serializer {
    void serialize(List<Particle> particles, int step, double dt);
}
