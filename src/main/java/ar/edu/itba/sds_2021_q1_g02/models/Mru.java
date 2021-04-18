package ar.edu.itba.sds_2021_q1_g02.models;

public class Mru {
    public static Double timeCalculation(double initPos, double destPos, double speed) {
        return speed != 0 ? (destPos - initPos) / speed : Double.POSITIVE_INFINITY;
    }

    public static double positionCalculation(double initPos, double time, double speed) {
        return initPos + time * speed;
    }
}
