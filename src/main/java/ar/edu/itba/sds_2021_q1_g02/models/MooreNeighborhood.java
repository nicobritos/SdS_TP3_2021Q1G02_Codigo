package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.ArrayList;
import java.util.List;

public class MooreNeighborhood {
    private final List<Position> positions;

    public MooreNeighborhood(final int r) {
        this.positions = createPositions(r);
    }

    public List<Position> getPositions() {
        return this.positions;
    }

    private static List<Position> createPositions(int r) {
        List<Position> positions = new ArrayList<>();
        for (int x = -1 * r; x <= r; x++) {
            for (int y = -1 * r; y <= r; y++) {
                if (!(x == 0 && y == 0)) {
                    positions.add(new Position(x, y));
                }
            }
        }
        return positions;
    }
}
