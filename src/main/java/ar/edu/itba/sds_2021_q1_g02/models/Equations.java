package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Equations {
    private static Equations instance;

    public static Equations getInstance() {
        if (instance == null)
            instance = new Equations();

        return instance;
    }

    private Equations() {
    }

    // returns the min time that it will collide with a wall
    public Pair<Double, Direction> collisionWall(Particle p, Dimen systemDimens) {
        double x0 = p.getPosition().getX();
        double y0 = p.getPosition().getY();
        double tc, tcAux, tcAux1;
        Direction wall;

        // collision with vertical walls
        tc = this.collisionTimeWithWall(systemDimens.getXvi(), systemDimens.getXvf(), x0, p.getRadius(),
                p.getVelocity().getxSpeed());
        wall = Direction.VERTICAL;

        // collision with horizontal walls
        tcAux = this.collisionTimeWithWall(systemDimens.getYvi(), systemDimens.getYvf(), y0, p.getRadius(),
                p.getVelocity().getySpeed());

        // compare vertical and horizontal time collision and keep the MIN
        if (tc > tcAux) {
            tc = tcAux;
            wall = Direction.HORIZONTAL;
        }

        // collision with intermediate walls (vertical)
        tcAux1 = this.collisionTimeWithIntermediateWall(p, systemDimens);
        if (tcAux1 != Double.POSITIVE_INFINITY) {
            tcAux = tcAux1;
        }

        // compare vertical and horizontal time collision and keep the MIN
        if (tc > tcAux) {
            tc = tcAux;
            wall = Direction.VERTICAL;
        }
        return new Pair<>(tc, wall);
    }

    // returns the min time that it will collide with another particle
    public Pair<Double, Particle> collisionParticles(Particle p, Collection<Particle> particles) {
        double tc, d, dVTimesdR, tcAux;
        Particle particle = null;
        tc = Double.POSITIVE_INFINITY;
        for (Particle p2 : particles) {
            d = this.d(p, p2);
            dVTimesdR = this.deltaVTimesdeltaR(p, p2);
            if (d >= 0 && dVTimesdR < 0 && !p2.equals(p)) {
                tcAux = (-(dVTimesdR + Math.sqrt(d)) / this.deltaVTimesdeltaV(p, p2));
                if (tcAux < tc) {
                    particle = p2;
                    tc = tcAux;
                }
            }
        }
        return new Pair<>(tc, particle);
    }

    public Collection<Particle> evolveParticlesPositions(Collection<Particle> particles, double tc) {
        for (Particle p : particles) {
            p.getPosition().setX(p.getPosition().getX() + p.getVelocity().getxSpeed() * tc);
            p.getPosition().setY(p.getPosition().getY() + p.getVelocity().getySpeed() * tc);
        }

        return particles;
    }

    public Velocity evolveParticleVelocity(Particle p, Direction direction) {
        return direction.equals(Direction.VERTICAL) ? new Velocity(-p.getVelocity().getxSpeed(),
                p.getVelocity().getySpeed()) :
                new Velocity(p.getVelocity().getxSpeed(), -p.getVelocity().getySpeed());
    }

    public Pair<Velocity, Velocity> evolveParticlesVelocities(Particle p1, Particle p2) {
        Velocity v1d = new Velocity(p1.getVelocity().getxSpeed() + (this.Jx(p1, p2) / p1.getMass()),
                p1.getVelocity().getySpeed() + (this.Jy(p1, p2) / p1.getMass()));
        Velocity v2d = new Velocity(p2.getVelocity().getxSpeed() - (this.Jx(p1, p2) / p2.getMass()),
                p2.getVelocity().getySpeed() - (this.Jy(p1, p2) / p2.getMass()));

        return new Pair<>(v1d, v2d);
    }

    public Double goThroughApertureTime(Particle p, Dimen systemDimens) {
        double tc = Double.POSITIVE_INFINITY;
        double x0 = p.getPosition().getX();
        if (p.getVelocity().getxSpeed() > 0 && x0 < systemDimens.getApertureX()) {
            tc = Mru.timeCalculation(x0, systemDimens.getApertureX() - p.getRadius(), p.getVelocity().getxSpeed());
        } else if (p.getVelocity().getxSpeed() < 0 && p.getPosition().getX() > systemDimens.getApertureX()) {
            tc = Mru.timeCalculation(x0, systemDimens.getApertureX() + p.getRadius(), p.getVelocity().getxSpeed());
        }

        return tc != Double.POSITIVE_INFINITY ?
                (this.getParticlePosByTime(tc, p).getX() >= 0.4 && this.getParticlePosByTime(tc, p).getX() <= 0.5) ?
                        tc : Double.POSITIVE_INFINITY : tc;
    }

    public double collisionTimeWithIntermediateWall(Particle p, Dimen systemDimens) {
        double tcAux = Double.POSITIVE_INFINITY, tcAux1;

        if (p.getVelocity().getxSpeed() > 0 && p.getPosition().getX() < systemDimens.getApertureX()) {
            tcAux1 = Mru.timeCalculation(p.getPosition().getX(), systemDimens.getApertureX() - p.getRadius(),
                    p.getVelocity().getxSpeed());

            if (this.getParticlePosByTime(tcAux1, p).getY() < systemDimens.getApertureYvi() || this.getParticlePosByTime(tcAux1, p).getY() > systemDimens.getApertureYvf()) {
                tcAux = tcAux1;
            }
        } else if (p.getVelocity().getxSpeed() < 0 && p.getPosition().getX() > systemDimens.getApertureX()) {
            tcAux1 = Mru.timeCalculation(p.getPosition().getX(), systemDimens.getApertureX() + p.getRadius(),
                    p.getVelocity().getxSpeed());

            if (this.getParticlePosByTime(tcAux1, p).getY() < systemDimens.getApertureYvi() || this.getParticlePosByTime(tcAux1, p).getY() > systemDimens.getApertureYvf()) {
                tcAux = tcAux1;
            }
        }

        return tcAux;
    }

    private double collisionTimeWithWall(double iWall, double fWall, double partPos, double partRadius,
                                         double partSpeed) {
        double tc;
        if (partSpeed > 0) {
            tc = Mru.timeCalculation(partPos, fWall - partRadius, partSpeed);
        } else {
            tc = Mru.timeCalculation(partPos, iWall + partRadius, partSpeed);
        }
        return tc;
    }

    private double J(Particle p1, Particle p2) {
        return (2 * p1.getMass() * p2.getMass() * this.deltaVTimesdeltaR(p1, p2)) / (this.tita(p1, p2) * (p1.getMass() + p2.getMass()));
    }

    private double Jx(Particle p1, Particle p2) {
        return this.J(p1, p2) * this.deltaX(p1, p2) / this.tita(p1, p2);
    }

    private double Jy(Particle p1, Particle p2) {
        return this.J(p1, p2) * this.deltaY(p1, p2) / this.tita(p1, p2);
    }

    private Velocity deltaV(Particle p1, Particle p2) {
        return new Velocity(this.deltaVx(p1, p2), this.deltaVy(p1, p2));
    }

    private Position deltaR(Particle p1, Particle p2) {
        return new Position(this.deltaX(p1, p2), this.deltaY(p1, p2));
    }

    private double tita(Particle p1, Particle p2) {
        return p1.getRadius() + p2.getRadius();
    }

    private double deltaVTimesdeltaV(Particle p1, Particle p2) {
        return Math.pow(this.deltaVx(p1, p2), 2) + Math.pow(this.deltaVy(p1, p2), 2);
    }

    private double deltaVTimesdeltaR(Particle p1, Particle p2) {
        return this.deltaVx(p1, p2) * this.deltaX(p1, p2) + this.deltaVy(p1, p2) * this.deltaY(p1, p2);
    }

    private double deltaVx(Particle p1, Particle p2) {
        return p2.getVelocity().getxSpeed() - p1.getVelocity().getxSpeed();
    }

    private double deltaVy(Particle p1, Particle p2) {
        return p2.getVelocity().getySpeed() - p1.getVelocity().getySpeed();
    }

    private double deltaX(Particle p1, Particle p2) {
        return p2.getPosition().getX() - p1.getPosition().getX();
    }

    private double deltaY(Particle p1, Particle p2) {
        return p2.getPosition().getY() - p1.getPosition().getY();
    }

    private double d(Particle p1, Particle p2) {
        return Math.pow(this.deltaVTimesdeltaR(p1, p2), 2) - this.deltaVTimesdeltaV(p1, p2) * (this.deltaRTimesdeltaR(p1, p2) - Math.pow(this.tita(p1, p2), 2));
    }

    private double deltaRTimesdeltaR(Particle p1, Particle p2) {
        return Math.pow(this.deltaX(p1, p2), 2) + Math.pow(this.deltaY(p1, p2), 2);
    }

    private double xyOverlap(Particle p1, Particle p2) {
        return Math.pow(this.deltaX(p1, p2), 2) + Math.pow(this.deltaY(p1, p2), 2);
    }

    private double radiusOverlap(Particle p1, Particle p2) {
        return Math.pow(p1.getRadius() + p2.getRadius(), 2);
    }

    private Position getParticlePosByTime(double time, Particle p) {
        Position newPos = new Position(0, 0);
        newPos.setX(Mru.positionCalculation(p.getPosition().getX(), time, p.getVelocity().getxSpeed()));
        newPos.setY(Mru.positionCalculation(p.getPosition().getY(), time, p.getVelocity().getySpeed()));
        return newPos;
    }
}
