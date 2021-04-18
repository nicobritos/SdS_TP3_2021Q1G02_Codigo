package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.util.Collection;
import java.util.List;

public class ConsoleSerializer implements Serializer {
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
    public void serialize(Collection<Particle> particles, int step, double dt) {
        System.out.println(this.stepFormatter.format(particles, step, dt));
        for (Particle p : particles) {
            System.out.println(this.particleFormatter.format(p, step, dt));
        }
        System.out.println("----------------------");
    }
}
