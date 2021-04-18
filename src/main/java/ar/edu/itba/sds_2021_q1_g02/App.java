package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.Color;
import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import ar.edu.itba.sds_2021_q1_g02.serializer.ConsoleSerializer;
import ar.edu.itba.sds_2021_q1_g02.serializer.OvitoSerializer;
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

public class App {
    private static final int CONSOLE_SERIALIZER_LIMIT = 50;

    public static void main(String[] args) throws ParseException, IOException {
        CommandParser.getInstance().parse(args);

        System.out.println("Parsing particles");
        Pair<List<Particle>, Integer> particles = ParticleParser.parseParticles(CommandParser.getInstance().getInputPath());

        Dimen dimen = new Dimen(0, 0.24, 0, 0.09, 0.05);
        GasDiffusion GD = new GasDiffusion(particles.getKey(), dimen);

        GD.addSerializer(new OvitoSerializer(
                (systemParticles, step, dt, abs) -> (systemParticles.size() + 2) + "\n" + "Properties=id:R:1:radius:R:1:pos:R:2:mass:R:1:color:R:3:transparency:R:1",
                (particle, step, dt) -> {
                    // id (1), radius (1), pos (2), size (1), color (3, RGB)";
                    String s = particle.getId() + "\t" +
                            particle.getRadius() + "\t" +
                            particle.getPosition().getX() + "\t" +
                            particle.getPosition().getY() + "\t" +
                            particle.getMass() + "\t";

                    if (particle.getRadius() > 0) {
                        Color color = getParticleColor(particle, dimen);
                        s += color.getRed() + "\t" +
                             color.getGreen() + "\t" +
                             color.getBlue() + "\t" +
                             "0.0";
                    } else {
                        s += "0.0\t0.0\t0.0\t1.0";
                    }

                    return s;
                },
                step -> "output/output_" + step + ".xyz",
                dimen
        ));

        if (particles.getKey().size() < CONSOLE_SERIALIZER_LIMIT) {
            GD.addSerializer(new ConsoleSerializer(
                    (systemParticles, systemDimen) -> {
                        return  "Height = " + String.format("%.5f", systemDimen.getYvf() - systemDimen.getYvi()) +
                                "m; Width = " + String.format("%.5f", systemDimen.getXvf() - systemDimen.getXvi()) +
                                "m; Aperture size = " + String.format("%.5f", systemDimen.getApertureYvf() - systemDimen.getApertureYvi()) +
                                "m; Aperture X position = " + String.format("%.5f", systemDimen.getApertureX()) +
                                "m";
                    },
                    (stepParticles, step, dt, absoluteTime) -> {
                        return "** Step = " + step +
                                " dT = " + String.format("%.5fs", dt) +
                                " abs = " + String.format("%.5fs", absoluteTime);
                    },
                    (particle, step, dt) -> {
                        return particle.getId() + " | " +
                                String.format("(%.5f, %.5f)m", particle.getPosition().getX(), particle.getPosition().getY()) + " | " +
                                String.format("(%.5f, %.5f)m", particle.getVelocity().getxSpeed(), particle.getVelocity().getySpeed());
                    }
            ));
        }

        System.out.println("Running simulation");
        GD.simulate(CommandParser.getInstance().getMaxIterations());
    }

    private static Color getParticleColor(Particle particle, Dimen dimen) {
        if (App.isParticleInLeftEnclosure(particle, dimen)) {
            return new Color(1.0, 0, 0);
        } else {
            return new Color(0, 1.0, 0);
        }
    }

    private static boolean isParticleInLeftEnclosure(Particle particle, Dimen dimen) {
        double normalizedX = particle.getPosition().getX() - dimen.getXvi();
        return normalizedX < dimen.getApertureX();
    }
}
