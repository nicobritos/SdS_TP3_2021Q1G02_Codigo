package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;

import java.io.*;
import java.util.List;

public class FileSerializer implements Serializer {
    private final ParticleFormatter particleFormatter;
    private final FileFormatter fileFormatter;

    public FileSerializer(ParticleFormatter particleFormatter, FileFormatter fileFormatter) {
        this.particleFormatter = particleFormatter;
        this.fileFormatter = fileFormatter;
    }

    @Override
    public void serialize(List<Particle> particles, int step, double dt) {
        File file = new File(this.fileFormatter.formatFilename(step));
        if (file.exists() && !file.delete())
            throw new RuntimeException("Couldn't delete file: " + file.getName());

        try {
            if (!file.createNewFile())
                throw new RuntimeException("Couldn't create file: " + file.getName());

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            bufferedWriter.write(this.fileFormatter.formatSystem(particles));
            bufferedWriter.write("\n");
            for (Particle p : particles) {
                bufferedWriter.write(this.particleFormatter.format(p, step, dt));
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
