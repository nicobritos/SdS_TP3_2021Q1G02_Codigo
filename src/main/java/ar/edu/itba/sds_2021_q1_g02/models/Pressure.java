package ar.edu.itba.sds_2021_q1_g02.models;

public class Pressure {
    public static double calculate(double initSpeed, double finalSpeed, double mass, double dt, double d) {
        return deltaP(initSpeed,finalSpeed,mass) / (dt * d);
    }

    private static double deltaP(double initSpeed, double finalSpeed, double mass) {
        return mass * finalSpeed - mass * initSpeed;
    }
}
