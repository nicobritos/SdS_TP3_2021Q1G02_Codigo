package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.util.List;

@FunctionalInterface
public interface ParticleFormatter {
    String format(Particle particle, int step, double dt);
}
