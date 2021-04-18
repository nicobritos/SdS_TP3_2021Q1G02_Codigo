package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.util.List;

public class ConsoleSerializer implements Serializer {
    private final SystemFormatter systemFormatter;
    private final ParticleFormatter particleFormatter;

    public ConsoleSerializer(SystemFormatter systemFormatter, ParticleFormatter particleFormatter) {
        this.systemFormatter = systemFormatter;
        this.particleFormatter = particleFormatter;
    }

    @Override
    public void serialize(List<Particle> particles, int step, double dt, Dimen systemDimen) {
        System.out.println(this.systemFormatter.format(particles, systemDimen, step, dt));
        for (Particle p : particles) {
            System.out.println(this.particleFormatter.format(p, step, dt));
        }
        System.out.println("----------------------");
    }
}
