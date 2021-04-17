package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.Dimen;
import ar.edu.itba.sds_2021_q1_g02.models.Direction;
import ar.edu.itba.sds_2021_q1_g02.models.Equations;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import javafx.util.Pair;

import java.util.*;

public class GasDiffusion {
    private final Dimen dimen;
    private final List<Particle> particles;

    public GasDiffusion(List<Particle> particles, Dimen dimen) {
        this.particles = particles;
        this.dimen = dimen;
    }

    public void simulate(int maxIterations) {
        int i = 0;

        while (i < maxIterations) {
            this.simulateStep();
            i++;
        }
    }

    private void simulateStep() {
        double minStepTime = Double.POSITIVE_INFINITY;
        List<Pair<Particle, Direction>> wallCollisions = new LinkedList<>();
        List<Pair<Particle, Particle>> particleCollisions = new LinkedList<>();

        for (Particle p : this.particles) {
            Pair<Double, Direction> wallCollision = Equations.getInstance().collisionWall(p, this.dimen);
            Pair<Double, Particle> particleCollision = Equations.getInstance().collisionParticles(p, this.particles);

            // If a wall collision happens before a particle collision
            if (wallCollision.getKey() < particleCollision.getKey()) {
                // If the wall collision happens before the first collision in the system
                if (wallCollision.getKey() < minStepTime) {
                    // Set new min time, clear lists and add the new collision
                    minStepTime = wallCollision.getKey();
                    particleCollisions.clear();
                    wallCollisions.clear();
                    wallCollisions.add(new Pair<>(p, wallCollision.getValue()));
                } else if (wallCollision.getKey() == minStepTime) {
                    // TODO: Probably use an epsilon (ex. e-10)
                    // Just add the new collision
                    wallCollisions.add(new Pair<>(p, wallCollision.getValue()));
                }
            } else if (particleCollision.getKey() < minStepTime) {
                // Set new min time, clear previous list and add the new collision
                minStepTime = particleCollision.getKey();
                particleCollisions.clear();
                wallCollisions.clear();
                particleCollisions.add(new Pair<>(p, particleCollision.getValue()));

                // TODO: Probably use an epsilon (ex. e-10)
                if (particleCollision.getKey().doubleValue() == wallCollision.getKey().doubleValue()) {
                    wallCollisions.add(new Pair<>(p, wallCollision.getValue()));
                }
            } else if (particleCollision.getKey() == minStepTime) {
                // TODO: Probably use an epsilon (ex. e-10)

                // Just add the new collision
                particleCollisions.add(new Pair<>(p, particleCollision.getValue()));

                // TODO: Probably use an epsilon (ex. e-10)
                if (particleCollision.getKey().doubleValue() == wallCollision.getKey().doubleValue()) {
                    wallCollisions.add(new Pair<>(p, wallCollision.getValue()));
                }
            }
        }

        Equations.getInstance().evolveParticlesPositions(this.particles, minStepTime);

        wallCollisions.forEach(particleDirectionPair -> {
            Equations.getInstance().evolveParticleVelocities(particleDirectionPair.getKey(), particleDirectionPair.getValue());
        });
        particleCollisions.forEach(particleParticlePair -> {
            Equations.getInstance().evolveParticlesVelocities(particleParticlePair.getKey(), particleParticlePair.getValue());
        });
    }
}
