package sokoban;

import gps.api.GPSState;
import model.Board;
import model.Position;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by sebastian on 3/22/17.
 */
public class SokobanState implements GPSState {

    private int heuristicValue;
    private Board board;

    public SokobanState(final Board board) {
        this.board = Objects.requireNonNull(board);
    }

    public Optional<GPSState> apply(SokobanRule rule) {
        // TODO: This could probably be done after checking if the movement is possible
        final Board auxBoard = board.duplicate();

        final Optional<Board> nextBoard = auxBoard.move(rule.x, rule.y);
        if (!nextBoard.isPresent())
            return Optional.empty();

        final GPSState nextState = new SokobanState(nextBoard.get());

        return Optional.of(nextState);
    }

    public int getValue() {
        return this.heuristicValue;
    }

    public Board getBoard() {
        return board;
    }

    // TODO: if the performance is bad, we should consider refactoring this method


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SokobanState that = (SokobanState) o;

        return board.equals(that.board);
    }

    @Override
    public int hashCode() {
        return board.hashCode();
    }
}
