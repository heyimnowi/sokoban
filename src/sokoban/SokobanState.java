package sokoban;

import gps.api.GPSState;
import model.Position;

import java.util.Set;

/**
 * Created by sebastian on 3/22/17.
 */
public class SokobanState implements GPSState {

    private int heuristicValue;
    private Position playerPosition;
    private Set<Position> boxesPositions;
    private Set<Position> goalsPositions;

    public SokobanState(int heuristicValue, Position playerPosition, Set<Position> boxesPositions, Set<Position> goalsPositions) {
        this.heuristicValue = heuristicValue;
        this.playerPosition = playerPosition;
        this.boxesPositions = boxesPositions;
        this.goalsPositions = goalsPositions;
    }

    public int getValue() {
        return this.heuristicValue;
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

}
