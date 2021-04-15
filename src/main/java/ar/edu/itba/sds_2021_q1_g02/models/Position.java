package ar.edu.itba.sds_2021_q1_g02.models;

public class Position {
    protected final double x;
    protected final double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }


    public Position copy() {
        return new Position(this.x, this.y);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [").append((int) this.x).append(",").append((int) this.y).append("] ");
        return sb.toString();
    }
}
