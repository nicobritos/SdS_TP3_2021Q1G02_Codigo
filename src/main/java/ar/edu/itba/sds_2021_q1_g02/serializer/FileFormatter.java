package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.util.Collection;

public interface FileFormatter {
    String formatFilename(int step);

    String formatSystem(Collection<Particle> particles);
}
