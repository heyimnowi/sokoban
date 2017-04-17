package model.heuristics;

import gps2.api.GPSState;

public interface Heuristic {

    /**
     * Implements
     * @param state
     * @return
     */
    int calculate(GPSState state);

}
