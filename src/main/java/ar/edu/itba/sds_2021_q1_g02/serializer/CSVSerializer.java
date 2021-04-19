package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Position;
import ar.edu.itba.sds_2021_q1_g02.models.Velocity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class CSVSerializer implements Serializer {
    private final StepFormatter stepFormatter;
    private final ParticleFormatter particleFormatter;
    private final FileFormatter fileFormatter;

    public CSVSerializer(StepFormatter stepFormatter, ParticleFormatter particleFormatter, FileFormatter fileFormatter) {
        this.stepFormatter = stepFormatter;
        this.particleFormatter = particleFormatter;
        this.fileFormatter = fileFormatter;
    }

    @Override
    public void serialize(Collection<Particle> particles, int step, double dt, double absoluteTime) {
        File file = new File(this.fileFormatter.formatFilename(step));
        if (file.exists() && !file.delete())
            throw new RuntimeException("Couldn't delete file: " + file.getName());

        try {
            if (!file.createNewFile())
                throw new RuntimeException("Couldn't create file: " + file.getName());

            FileWriter writer = new FileWriter(file);

            writer.write(this.stepFormatter.format(particles, step, dt, absoluteTime));
            writer.write("\n");
            for (Particle p : particles) {
                writer.write(this.particleFormatter.format(p, step, dt, absoluteTime));
                writer.write("\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
