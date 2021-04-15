package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Wall {
    private final int id;
    private double position;

    public Wall(int id, double position) {
        this.position = position;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public double getPosition() {
        return this.position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wall)) return false;
        Wall wall = (Wall) o;
        return this.getId() == wall.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
