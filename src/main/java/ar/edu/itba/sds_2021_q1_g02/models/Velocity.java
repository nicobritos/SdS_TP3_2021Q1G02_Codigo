package ar.edu.itba.sds_2021_q1_g02.models;

public class Velocity {
    private final double xSpeed;
    private final double ySpeed;

    public Velocity(double xSpeed, double ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public double getxSpeed() {
        return this.xSpeed;
    }

    public double getySpeed() {
        return this.ySpeed;
    }

    public Velocity copy() {
        return new Velocity(this.xSpeed, this.ySpeed);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [").append(this.xSpeed).append(",").append(this.ySpeed).append("] ");
        return sb.toString();
    }
}
