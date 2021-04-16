package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws ParseException, IOException {
        CommandParser.getInstance().parse(args);
        System.out.println("Parsing particles");
        Pair<List<Particle>, Integer> particles = ParticleParser.parseParticles(CommandParser.getInstance().getInputPath());
        GasDiffusion GD = new GasDiffusion(particles.getKey(), particles.getValue());

        System.out.println("Running simulation");
        GD.simulate(CommandParser.getInstance().getMaxIterations());

    }
}
