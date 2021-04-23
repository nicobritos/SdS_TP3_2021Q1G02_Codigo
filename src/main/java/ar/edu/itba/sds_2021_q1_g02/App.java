package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.Color;
import ar.edu.itba.sds_2021_q1_g02.models.Configuration;
import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import ar.edu.itba.sds_2021_q1_g02.serializer.CSVSerializer;
import ar.edu.itba.sds_2021_q1_g02.serializer.ConsoleSerializer;
import ar.edu.itba.sds_2021_q1_g02.serializer.OvitoSerializer;
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

public class App {
    private final static double OCCUPATION_FACTOR_TOLERANCE = 0.01;
    private final static double DT = 100;

    public static void main(String[] args) throws ParseException, IOException {
        CommandParser.getInstance().parse(args);

        System.out.println("Parsing particles");
        Pair<List<Particle>, Integer> particles =
                ParticleParser.parseParticles(CommandParser.getInstance().getInputPath());

        Dimen dimen = new Dimen(0, 0.24, 0, 0.09, 0.01);
        GasDiffusion GD = new GasDiffusion(particles.getKey(), new Configuration(dimen, OCCUPATION_FACTOR_TOLERANCE,
                DT));

        GD.addSerializer(new OvitoSerializer(
                (systemParticles, step) -> (systemParticles.size() + 2) + "\n" + "Properties=id:R:1:radius:R:1:pos:R" +
                        ":2:Velocity:R:2:mass:R:1:color:R:3:transparency:R:1",
                (particle, step) -> {
                    // id (1), radius (1), pos (2), size (1), color (3, RGB)";
                    String s = particle.getId() + "\t" +
                            particle.getRadius() + "\t" +
                            particle.getPosition().getX() + "\t" +
                            particle.getPosition().getY() + "\t" +
                            particle.getVelocity().getxSpeed() + "\t" +
                            particle.getVelocity().getySpeed() + "\t" +
                            particle.getMass() + "\t";

                    if (particle.getId() >= 0) {
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
                step -> "R:/output/output_" + step + ".xyz",
                dimen
        ));

        GD.addSerializer(new CSVSerializer(
                (systemParticles, step) -> "id;\"radius [m]\";\"x [m]\";\"y [m]\";\"mass [kg]\";\"vx [m/s]\";\"vy " +
                        "[m/s]\";\"dt [s]\";\"t [s]\";fp",
                (particle, step) -> {
                    // id (1), radius (1), pos (2), size (1), speed (2), dt (1), t (1)";
                    return particle.getId() + ";" +
                            particle.getRadius() + ";" +
                            particle.getPosition().getX() + ";" +
                            particle.getPosition().getY() + ";" +
                            particle.getMass() + ";" +
                            particle.getVelocity().getxSpeed() + ";" +
                            particle.getVelocity().getySpeed() + ";" +
                            step.getRelativeTime() + ";" +
                            step.getAbsoluteTime() + ";" +
                            step.getLeftOccupationFactor();
                },
                step -> "R:/output/output_" + step + ".csv"
        ));

        GD.addSerializer(new ConsoleSerializer(
                (systemParticles, configuration) -> {
                    return "Height = " + String.format("%.5f",
                            configuration.getDimen().getYvf() - configuration.getDimen().getYvi()) +
                            "m; Width = " + String.format("%.5f",
                            configuration.getDimen().getXvf() - configuration.getDimen().getXvi()) +
                            "m; Aperture size = " + String.format("%.5f",
                            configuration.getDimen().getApertureYvf() - configuration.getDimen().getApertureYvi()) +
                            "m; Aperture X position = " + String.format("%.5f",
                            configuration.getDimen().getApertureX()) +
                            "m; Occupation factor tolerance = " + String.format("%.5f",
                            configuration.getOccupationFactor());
                },
                (stepParticles, step) -> {
                    return "** Step = " + step.getStep() +
                            "; dT = " + String.format("%.5fs", step.getRelativeTime()) +
                            "; abs = " + String.format("%.5fs", step.getAbsoluteTime()) +
                            "; fp = " + String.format("%.5f", step.getLeftOccupationFactor());
                },
                (particle, step) -> {
                    return particle.getId() + " | " +
                            String.format("(%.5f, %.5f)m", particle.getPosition().getX(),
                                    particle.getPosition().getY()) + " | " +
                            String.format("(%.5f, %.5f)m/s", particle.getVelocity().getxSpeed(),
                                    particle.getVelocity().getySpeed());
                }
        ));

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
