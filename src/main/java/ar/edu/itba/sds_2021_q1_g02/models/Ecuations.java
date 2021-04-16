package ar.edu.itba.sds_2021_q1_g02.models;

public class Ecuations {
    public boolean NoOverlap(Particle p1){
        for(Particle p2 : p1.getNeighbors()){
            if(xyOverlap(p1,p2) < radiusOverlap(p1,p2))){
                return false;
            }
        }
        return true;
    }
    private double xyOverlap(Particle p1, Particle p2){
        return Math.pow(p1.getPosition().x - p2.getPosition().x, 2) +Math.pow(p1.getPosition().y - p2.getPosition().y, 2)
    }
    private double radiusOverlap(Particle p1, Particle p2){
        return Math.pow(p1.getRadius() + p2.getRadius(), 2) ;
    }
    // returns the min time that it will collide with a wall
    public double CollitionWall(Particle p){
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

    private Position getParticlePos(double time, Particle p){
        Position newPos = new Position(0,0);
        newPos.setX(p.getPosition().getX() + time * p.getVelocity().getxSpeed());
        newPos.setY(p.getPosition().getY() + time * p.getVelocity().getySpeed());
        return newPos;
    }
}
