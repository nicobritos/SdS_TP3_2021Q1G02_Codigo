package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.Color;
import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import ar.edu.itba.sds_2021_q1_g02.serializer.FileFormatter;
import ar.edu.itba.sds_2021_q1_g02.serializer.FileSerializer;
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class App {
    public static void main(String[] args) throws ParseException, IOException {
        CommandParser.getInstance().parse(args);

        System.out.println("Parsing particles");
        Pair<List<Particle>, Integer> particles = ParticleParser.parseParticles(CommandParser.getInstance().getInputPath());

        Dimen dimen = new Dimen(0, 0.24, 0, 0.09, 0.05);
        GasDiffusion GD = new GasDiffusion(particles.getKey(), dimen);

        FileSerializer serializer = new FileSerializer(
                (particle, step, dt) -> {
                    Color color = getParticleColor(particle, dimen);
                    // id (1), radius (1), pos (2), size (1), color (3, RGB)";
                    return  particle.getId() + "\t" +
                            particle.getRadius() + "\t" +
                            particle.getPosition().getX() + "\t" +
                            particle.getPosition().getY() + "\t" +
                            particle.getMass() + "\t" +
                            color.getRed() + "\t" +
                            color.getGreen() + "\t" +
                            color.getBlue() + "\t";
                },
                new FileFormatterImpl()
        );
        GD.addSerializer(serializer);

        System.out.println("Running simulation");
        GD.simulate(CommandParser.getInstance().getMaxIterations());
    }

    private static Color getParticleColor(Particle particle, Dimen dimen) {
        double normalizedX = particle.getPosition().getX() - dimen.getXvi();

        if (normalizedX < dimen.getApertureX()) {
            return new Color(1.0, 0, 0);
        } else {
            return new Color(0, 1.0, 0);
        }
    }

    private static class FileFormatterImpl implements FileFormatter {
        @Override
        public String formatFilename(int step) {
            return "output_" + step + ".xyz";
        }

        @Override
        public String formatSystem(Collection<Particle> particles) {
            return particles.size() + "\n" + "Properties=id:R:1:radius:R:1:pos:R:2:mass:R:1:color:R:3";
        }
    }
}
