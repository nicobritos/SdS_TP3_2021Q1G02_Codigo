package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.util.Collection;

public class ConsoleSerializer implements Serializer {
    private static final int CONSOLE_SERIALIZER_LIMIT = 50;

    private final SystemFormatter systemFormatter;
    private final StepFormatter stepFormatter;
    private final ParticleFormatter particleFormatter;

    public ConsoleSerializer(SystemFormatter systemFormatter, StepFormatter stepFormatter, ParticleFormatter particleFormatter) {
        this.systemFormatter = systemFormatter;
        this.stepFormatter = stepFormatter;
        this.particleFormatter = particleFormatter;
    }

    @Override
    public void serializeSystem(Collection<Particle> particles, Dimen systemDimen) {
        System.out.println(this.systemFormatter.format(particles, systemDimen));
    }

    @Override
    public void serialize(Collection<Particle> particles, int step, double dt, double absoluteTime) {
        System.out.println(this.stepFormatter.format(particles, step, dt, absoluteTime));

        if (particles.size() < CONSOLE_SERIALIZER_LIMIT) {
            for (Particle p : particles) {
                System.out.println(this.particleFormatter.format(p, step, dt, absoluteTime));
            }
        }

        System.out.println("----------------------");
    }
}
