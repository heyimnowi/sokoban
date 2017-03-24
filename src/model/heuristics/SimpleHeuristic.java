package model.heuristics;

import gps.api.GPSState;
import model.Board;
import model.Position;
import sokoban.SokobanState;

import java.util.Set;

public class SimpleHeuristic implements Heuristic {

    @Override
    public int calculate(GPSState state) {
        final Board board = ((SokobanState) state).getBoard();
        final Set<Position> boxes = board.getBoxesPosition();
        final int totalBoxes = board.getBoxesPosition().size();
        final int boxesInPlace = (int) boxes.stream()
                .filter(position -> board.getCellAt(position).isGoal())
                .count();

        return totalBoxes - boxesInPlace;
    }
}
