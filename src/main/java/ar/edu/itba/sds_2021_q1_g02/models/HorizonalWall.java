package ar.edu.itba.sds_2021_q1_g02.models;

public class HorizonalWall extends Wall{
    public HorizonalWall(int id, double position) {
        super(id, position);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorizonalWall)) return false;
        HorizonalWall wall = (HorizonalWall) o;
        return this.getId() == wall.getId();
    }
}
