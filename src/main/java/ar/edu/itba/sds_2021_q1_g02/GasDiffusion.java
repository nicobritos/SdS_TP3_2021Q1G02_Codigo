package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Direction;
import ar.edu.itba.sds_2021_q1_g02.models.Equations;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import javafx.util.Pair;

import java.lang.reflect.Parameter;
import java.util.*;

public class GasDiffusion {
    Dimen dimen = new Dimen(0, 0.24, 0, 0.09, 0.01);
    int M;
    List<Particle> particles;

    public GasDiffusion(List<Particle> particles, final int M) {
        this.particles = particles;
        this.M = M;
    }

    public void simulate(int maxIterations) {
        List<Pair<Double, Direction>> tcList = new ArrayList<Pair<Double, Direction>>();
        List<Double> timeList = new ArrayList<Double>();
        Equations eq = new Equations();
        for (Particle p : this.particles) {
            Pair<Double, Direction> tc = eq.CollisionWall(p, this.dimen);
            tcList.add(tc);
            timeList.add(tc.getKey());
            //estaba checkeando nomas que este bien me voy  a clase we
            if (tc.getKey() == -0.0) {
                System.out.println(p.getPosition().getX());
                System.out.println(p.getVelocity().getxSpeed());
            }
        }
        Collections.sort(timeList);
        System.out.println(tcList);


    }
}
