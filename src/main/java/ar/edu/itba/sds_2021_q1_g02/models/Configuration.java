package ar.edu.itba.sds_2021_q1_g02.models;

public class Configuration {
    private final Dimen dimen;
    private final double occupationFactor;
    private final double dt;

    public Configuration(Dimen dimen, double occupationFactor, double dt) {
        this.dimen = dimen;
        this.occupationFactor = occupationFactor;
        this.dt = dt;

    }

    public Dimen getDimen() {
        return this.dimen;
    }

    public double getOccupationFactor() {
        return this.occupationFactor;
    }

    public double getDt() {
        return this.dt;
    }
}


