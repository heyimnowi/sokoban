package sokoban;

import gps.api.GPSProblem;
import gps.api.GPSRule;
import gps.api.GPSState;
import model.Board;
import model.Position;
import model.heuristics.Heuristic;
import utils.BoardReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by sebastian on 3/22/17.
 */
public class SokobanProblem implements GPSProblem {

    private GPSState initState;
    private Board solutionBoard;
    private Heuristic heuristic;

    public SokobanProblem(String path, Heuristic heuristic) {
        try {
            final Board board = BoardReader.fromFile(path);

            this.heuristic = Objects.requireNonNull(heuristic);

            initState = new SokobanState(board);
            solutionBoard = board.getSolutionBoard();
        } catch (IOException ex) {
            // TODO
        }
    }

    @Override
    public GPSState getInitState() {
        return initState;
    }

    @Override
    public boolean isGoal(GPSState state) {
        final SokobanState thatState = (SokobanState) state;
        final Set<Position> thisBoxes = solutionBoard.getBoxesPosition();
        final Set<Position> thatBoxes = thatState.getBoard().getBoxesPosition();

        return thisBoxes.equals(thatBoxes);
    }

    @Override
    public List<GPSRule> getRules() {
        return Arrays.asList(SokobanRule.DOWN, SokobanRule.UP, SokobanRule.LEFT, SokobanRule.RIGHT);
    }

    @Override
    public Integer getHValue(GPSState state) {
        return heuristic.calculate(state);
    }
}
