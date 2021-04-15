package ar.edu.itba.sds_2021_q1_g02.models;

public class VerticalWall extends Wall{
    public VerticalWall(int id, double position) {
        super(id, position);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerticalWall)) return false;
        VerticalWall wall = (VerticalWall) o;
        return this.getId() == wall.getId();
    }
}
