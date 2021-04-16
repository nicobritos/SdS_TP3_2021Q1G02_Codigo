package ar.edu.itba.sds_2021_q1_g02.parsers;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Position;
import ar.edu.itba.sds_2021_q1_g02.models.Velocity;
import jdk.internal.net.http.common.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class ParticleParser {
    public static Pair<List<Particle>, Integer> parseParticles(String filePath) throws IOException {
        List<Particle> particles;

        int i = 0, M;
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);

            String data = myReader.nextLine();
            M = Integer.parseInt(data);

            particles = new ArrayList<>((int) (Math.pow(M, 3)));

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                String[] info = data.split("\t");
                // int id, double radius,double mass,Position position, Velocity velocity
                Particle particle = new Particle(i,Double.parseDouble(info[0]), Integer.parseInt(info[1]), new Position(Double.parseDouble(info[2]), Double.parseDouble(info[3])), new Velocity(Double.parseDouble(info[4]), Double.parseDouble(info[5])));
                particles.add(particle);
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            throw e;
        }

        return new Pair<>(particles, M);
    }
}
