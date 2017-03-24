package sokoban;

import gps.api.GPSRule;
import gps.api.GPSState;

import java.util.Optional;

public enum SokobanRule implements GPSRule {

    LEFT(-1, 0),
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1);

    public int x;
    public int y;

    SokobanRule(int x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public Integer getCost() {
        return 1;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public Optional<GPSState> evalRule(GPSState state) {
        final SokobanState sokobanState = (SokobanState) state;
        return sokobanState.apply(this);
    }


}
