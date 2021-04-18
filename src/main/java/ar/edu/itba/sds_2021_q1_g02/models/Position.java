package ar.edu.itba.sds_2021_q1_g02.models;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }
    public void setX(double x) { this.x  = x;}

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Position copy() {
        return new Position(this.x, this.y);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [").append(this.x).append(",").append(this.y).append("] ");
        return sb.toString();
    }
}
