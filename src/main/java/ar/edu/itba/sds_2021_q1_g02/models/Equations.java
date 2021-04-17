package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Equations {
    // returns the min time that it will collide with a wall
    public Pair<Double, String> CollisionWall(Particle p) {
        double x0 = p.getPosition().getX();
        double y0 = p.getPosition().getY();
        double xWall, yWall, tc, tcAux, tcAux1;
        String wall;

        // collision with vertical walls
        if (p.getVelocity().getxSpeed() > 0) {
            xWall = 0.24;
            tc = (xWall - p.getRadius() - x0) / p.getVelocity().getxSpeed();
        } else {
            xWall = 0;
            tc = (xWall + p.getRadius() - x0) / p.getVelocity().getxSpeed();
        }
        wall = "vertical";
        // collision with horizontal walls
        if (p.getVelocity().getySpeed() > 0) {
            yWall = 0.9;
            tcAux = (yWall - p.getRadius() - y0) / p.getVelocity().getySpeed();
        } else {
            yWall = 0;
            tcAux = (yWall + p.getRadius() - y0) / p.getVelocity().getySpeed();
        }
        if (tc > tcAux) {
            tc = tcAux;
            wall = "horizontal";
        }

        // collision with intermediate walls (vertical)
        if (p.getVelocity().getxSpeed() > 0 && p.getPosition().getX() < 0.12) {
            xWall = 0.12;
            tcAux1 = (xWall - p.getRadius() - x0) / p.getVelocity().getxSpeed();
            if (this.getParticlePos(tcAux1, p).getX() > 0.4 || this.getParticlePos(tcAux1, p).getX() < 0.5) {
                tcAux = tcAux1;
            }
        } else if (p.getVelocity().getxSpeed() < 0 && p.getPosition().getX() > 0.12) {
            xWall = 0.12;
            tcAux1 = (xWall + p.getRadius() - x0) / p.getVelocity().getxSpeed();
            if (this.getParticlePos(tcAux1, p).getX() > 0.4 || this.getParticlePos(tcAux1, p).getX() < 0.5) {
                tcAux = tcAux1;
            }
        }
        if (tc > tcAux) {
            tc = tcAux;
            wall = "vertical";
        }
        return new Pair<>(tc, wall);
    }

    public Pair<Double, Particle> CollisionParticles(Particle p, Collection<Particle> particles) {
        double tc, d, dTimesR, tcAux;
        Particle particle = null;
        tc = Double.POSITIVE_INFINITY;
        for (Particle p2 : particles) {
            d = this.d(p, p2);
            dTimesR = this.deltaVTimesdeltaR(p, p2);
            if (this.d(p, p2) > 0 && dTimesR < 0 && !p2.equals(p)) {
                tcAux = (-dTimesR + Math.sqrt(d)) / this.deltaVTimesdeltaV(p, p2);
                if (tcAux < tc) {
                    particle = p2;
                    tc = tcAux;
                }
            }
        }
        return new Pair<>(tc, particle);
    }

    public Collection<Particle> EvolveParticlesPositions(Collection<Particle> particles, double tc) {
        for (Particle p : particles) {
            p.getPosition().setX(p.getPosition().getX() + p.getVelocity().getxSpeed() * tc);
            p.getPosition().setY(p.getPosition().getY() + p.getVelocity().getySpeed() * tc);
        }
        return particles;
    }

    public Velocity EvolveParticleVelocities(Particle p1, boolean vertical) {
        if (vertical) {
            return new Velocity(-p1.getVelocity().getxSpeed(), p1.getVelocity().getySpeed());
        }
        return new Velocity(p1.getVelocity().getxSpeed(), -p1.getVelocity().getySpeed());
    }

    public List<Velocity> EvolveParticleVelocities(Particle p1, Particle p2) {
        Velocity v1d = new Velocity(p1.getVelocity().getxSpeed() + (this.Jx(p1, p2) / p1.getMass()), p1.getVelocity().getySpeed() + (this.Jy(p1, p2) / p1.getMass()));
        Velocity v2d = new Velocity(p2.getVelocity().getxSpeed() - (this.Jx(p1, p2) / p2.getMass()), p2.getVelocity().getySpeed() - (this.Jy(p1, p2) / p2.getMass()));
        List<Velocity> v = new ArrayList<>();
        v.add(v1d);
        v.add(v2d);
        return v;
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
        return this.xyOverlap(p1, p2);
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

    private Position getParticlePos(double time, Particle p) {
        Position newPos = new Position(0, 0);
        newPos.setX(p.getPosition().getX() + time * p.getVelocity().getxSpeed());
        newPos.setY(p.getPosition().getY() + time * p.getVelocity().getySpeed());
        return newPos;
    }
}
