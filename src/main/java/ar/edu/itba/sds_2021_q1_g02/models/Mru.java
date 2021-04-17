package ar.edu.itba.sds_2021_q1_g02.models;

public class Mru {
    public static double timeCalculation(double initPos, double destPos, double speed) {
        return (destPos - initPos) / speed;
    }

    public static double positionCalculation(double initPos, double time, double speed) {
        return initPos + time * speed;
    }
}
