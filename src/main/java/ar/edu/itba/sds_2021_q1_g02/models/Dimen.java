package ar.edu.itba.sds_2021_q1_g02.models;

public class Dimen {
    private double xvi;
    private double xvf;
    private double yvi;
    private double yvf;
    private double apertureX;
    private double apertureYvi;
    private double apertureYvf;

    public Dimen(double xvi, double xvf, double yvi, double yvf, double apertureWidth) {
        this.xvi = xvi;
        this.xvf = xvf;
        this.yvi = yvi;
        this.yvf = yvf;
        this.apertureX = (xvf - xvi) / 2.0;
        this.apertureYvi = ((yvf - yvi) / 2.0) - (apertureWidth / 2.0);
        this.apertureYvf = ((yvf - yvi) / 2.0) + (apertureWidth / 2.0);
    }

    public double getXvi() {
        return this.xvi;
    }

    public double getXvf() {
        return this.xvf;
    }

    public double getYvi() {
        return this.yvi;
    }

    public double getYvf() {
        return this.yvf;
    }

    public double getApertureX() {
        return this.apertureX;
    }

    public double getApertureYvi() {
        return this.apertureYvi;
    }

    public double getApertureYvf() {
        return this.apertureYvf;
    }
}


