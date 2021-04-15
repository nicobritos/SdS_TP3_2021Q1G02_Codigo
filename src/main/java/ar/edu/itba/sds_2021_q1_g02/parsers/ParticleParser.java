package ar.edu.itba.sds_2021_q1_g02.parsers;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class ParticleParser {
//    public static Pair<List<Particle>, Integer> parseParticles(String filePath, boolean is3D) throws IOException {
//        List<Particle> particles;
//
//        int i = 0, M;
//        try {
//            File myObj = new File(filePath);
//            Scanner myReader = new Scanner(myObj);
//
//            String data = myReader.nextLine();
//            M = Integer.parseInt(data);
//
//            particles = new ArrayList<>((int) (is3D ? Math.pow(M, 2) : Math.pow(M, 3)));
//
//            while (myReader.hasNextLine()) {
//                data = myReader.nextLine();
//                String[] info = data.split("\t");
//                Particle particle = new Particle(i, Double.parseDouble(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[is3D ? 5 : 4]) == 1);
//                particles.add(particle);
//
//                if (is3D)
//                    particles.get(i).setPosition(new Position(Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[4])));
//                else
//                    particles.get(i).setPosition(new Position(Integer.parseInt(info[2]), Integer.parseInt(info[3])));
//
//                i++;
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            throw e;
//        }
//
//        return new Pair<>(particles, M);
//    }
}
//