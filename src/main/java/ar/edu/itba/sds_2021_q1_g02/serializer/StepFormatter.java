package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.util.Collection;

@FunctionalInterface
public interface StepFormatter {
    String format(Collection<Particle> stepParticles, int step, double dt);
}
