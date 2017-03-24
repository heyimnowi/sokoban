package model.heuristics;

import gps.api.GPSState;

public interface Heuristic {

    /**
     * Implements
     * @param state
     * @return
     */
    int calculate(GPSState state);

}
