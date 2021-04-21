package ar.edu.itba.sds_2021_q1_g02.models;

public class Configuration {
    private final Dimen dimen;
    private final double occupationFactor;

    public Configuration(Dimen dimen, double occupationFactor) {
        this.dimen = dimen;
        this.occupationFactor = occupationFactor;
    }

    public Dimen getDimen() {
        return this.dimen;
    }

    public double getOccupationFactor() {
        return this.occupationFactor;
    }
}


