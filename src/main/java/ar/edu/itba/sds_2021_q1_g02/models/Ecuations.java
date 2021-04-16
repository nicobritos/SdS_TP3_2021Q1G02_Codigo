package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Collection;

public class Ecuations {
    public boolean NoOverlap(Particle p1){
        for(Particle p2 : p1.getNeighbors()){
            if(xyOverlap(p1,p2) < radiusOverlap(p1,p2)){
                return false;
            }
        }
        return true;
    }
    // returns the min time that it will collide with a wall
    public double CollisionWall(Particle p){
        double x0 = p.getPosition().getX();
        double y0 = p.getPosition().getY();
        double xWall, yWall, tc, tcAux, tcAux1;

        // collition with vertical walls
        if(p.getVelocity().getxSpeed() > 0){
            xWall = 0.24;
            tc = (xWall - p.getRadius() - x0)/p.getVelocity().getxSpeed();
        }
        else{
            xWall = 0;
            tc = (xWall + p.getRadius() - x0)/p.getVelocity().getxSpeed();
        }
        // collition with horizontal walls
        if(p.getVelocity().getySpeed() > 0){
            yWall = 0.9;
            tcAux = (yWall - p.getRadius() - y0)/p.getVelocity().getySpeed();
        }
        else {
            yWall = 0;
            tcAux = (yWall + p.getRadius() - y0)/p.getVelocity().getySpeed();
        }
        if(tc>tcAux){ tc = tcAux;}

        // collition with intermediate walls (vertical)
        if(p.getVelocity().getxSpeed() > 0 && p.getPosition().getX() < 0.12){
            xWall = 0.12;
            tcAux1 = (xWall - p.getRadius() - x0)/p.getVelocity().getxSpeed();
            if(getParticlePos(tcAux1, p).getX() > 0.4 || getParticlePos(tcAux1, p).getX()< 0.5){
                tcAux = tcAux1;
            }
        }
        else if (p.getVelocity().getxSpeed() < 0 && p.getPosition().getX() > 0.12){
            xWall = 0.12;
            tcAux1 = (xWall + p.getRadius() - x0)/p.getVelocity().getxSpeed();
            if(getParticlePos(tcAux1, p).getX() > 0.4 || getParticlePos(tcAux1, p).getX()< 0.5){
                tcAux = tcAux1;
            }
        }
        if(tc>tcAux){ tc = tcAux;}
        return tc;
    }

    public double CollisionParticles(Particle p, Collection<Particle> particles){
        double tc,d, dTimesR, tcAux;
        tc = Double.POSITIVE_INFINITY;
        boolean firstParticle = true;
        for (Particle p2 : particles){
            d = d(p,p2);
            dTimesR =deltaVTimesdeltaR(p,p2);
            if(d(p,p2)> 0 && dTimesR<0 && !p2.equals(p)){
                tcAux = (- dTimesR + Math.sqrt(d))/deltaVTimesdeltaV(p,p2);
                if(firstParticle || tcAux < tc ){
                    firstParticle = false;
                    tc = tcAux;
                }
            }
        }
        return tc;
    }


    private Velocity deltaV(Particle p1, Particle p2){
        return  new Velocity(deltaVx(p1,p2), deltaVy(p1,p2));
    }
    private Position deltaR(Particle p1, Particle p2){
        return  new Position(deltaX(p1,p2), deltaY(p1,p2));
    }
    private double tita(Particle p1, Particle p2){
        return p1.getRadius() + p2.getRadius();
    }
    private double deltaVTimesdeltaV(Particle p1, Particle p2){
        return xyOverlap(p1,p2);
    }
    private double deltaVTimesdeltaR(Particle p1, Particle p2){
        return deltaVx(p1,p2)*deltaX(p1,p2)+ deltaVy(p1,p2)*deltaY(p1,p2);
    }
    private double deltaVx(Particle p1, Particle p2){
        return p2.getVelocity().getxSpeed() - p1.getVelocity().getxSpeed();
    }
    private double deltaVy(Particle p1, Particle p2){
        return p2.getVelocity().getySpeed() - p1.getVelocity().getySpeed();
    }
    private double deltaX(Particle p1, Particle p2){
        return p2.getPosition().getX() - p1.getPosition().getX();
    }
    private double deltaY(Particle p1, Particle p2){
        return p2.getPosition().getY() - p1.getPosition().getY();
    }
    private double d(Particle p1, Particle p2){
        return Math.pow(deltaVTimesdeltaR(p1,p2), 2)-deltaVTimesdeltaV(p1,p2)*(deltaRTimesdeltaR(p1,p2) - Math.pow(tita(p1,p2), 2));
    }
    private double deltaRTimesdeltaR(Particle p1, Particle p2){
        return Math.pow(deltaX(p1,p2),2)+ Math.pow(deltaY(p1,p2),2);
    }
    private double xyOverlap(Particle p1, Particle p2){
        return Math.pow(deltaX(p1,p2), 2) +Math.pow(deltaY(p1,p2), 2);
    }
    private double radiusOverlap(Particle p1, Particle p2){
        return Math.pow(p1.getRadius() + p2.getRadius(), 2) ;
    }
    private Position getParticlePos(double time, Particle p){
        Position newPos = new Position(0,0);
        newPos.setX(p.getPosition().getX() + time * p.getVelocity().getxSpeed());
        newPos.setY(p.getPosition().getY() + time * p.getVelocity().getySpeed());
        return newPos;
    }


}
