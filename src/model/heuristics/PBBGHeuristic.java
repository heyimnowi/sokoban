package model.heuristics;

import gps.api.GPSState;
import model.Board;
import model.Position;
import sokoban.SokobanState;

import java.util.Set;

public class PBBGHeuristic implements Heuristic {

    @Override
    public int calculate(final GPSState state) {
        final Board board = ((SokobanState) state).getBoard();
        final Position playerPosition = board.getPlayerPosition();
        final Set<Position> boxesPosition = board.getBoxesPosition();
        final Set<Position> goalsPosition = board.getGoalsPosition();

        final int playerBoxDistance = boxesPosition.stream()
                .filter(position -> !board.getCellAt(position).isGoal())
                .map(position -> playerPosition.distanceTo(position))
                .reduce((aDouble, aDouble2) -> aDouble + aDouble2)
                .orElse(0);

        final int boxesGoalDistance = boxesPosition.stream()
                .filter(position -> !board.getCellAt(position).isGoal())
                .map(boxPosition -> {
                    return goalsPosition.stream()
                            .map(goalPosition -> boxPosition.distanceTo(goalPosition))
                            .reduce((aDouble, aDouble2) -> aDouble + aDouble2)
                            .orElse(0);
                })
                .reduce((r, r2) -> r + r2)
                .orElse(0);

        return playerBoxDistance + boxesGoalDistance;
    }
}
