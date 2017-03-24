package model;


import model.cells.Cell;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by sebastian on 3/22/17.
 */
public class Board implements Duplicable<Board> {

    private Cell[][] cells;
    private Position playerPosition;
    private Set<Position> boxesPosition = new HashSet<>();
    private Board solutionBoard; // lazy initialization

    public Board(final Cell[][] cells) {
        this.cells = Objects.requireNonNull(cells);

        IntStream.range(0, cells.length).forEach(y ->
                IntStream.range(0, cells[y].length)
                        .forEach(x -> {
                            final Cell cell = cells[y][x];
                            if (playerPosition == null && cell.hasPlayer()) {
                                playerPosition = new Position(x, y);
                            }

                            if (cell.hasBox()) {
                                boxesPosition.add(new Position(x, y));
                            }
                        })
        );
    }

    public Optional<Board> move(final int x, final int y) {
        final Cell[][] newCells = cells.clone();
        final Position nextPosition = playerPosition.add(x, y);
        final Cell nextCell = getCellAt(newCells, nextPosition);

        if (nextCell.isWall())
            return Optional.empty();

        if (nextCell.hasBox()) {
            final Position nextBoxPosition = nextPosition.add(x, y);
            final Cell nextBoxCell = getCellAt(nextBoxPosition);

            if (nextBoxCell.isWall() || nextBoxCell.hasBox())
                return Optional.empty();
            else {
                getCellAt(newCells, nextPosition).setBox(false);
                getCellAt(newCells, nextBoxPosition).setBox(true);
            }
        }

        newCells[playerPosition.y][playerPosition.x].setPlayer(false);
        newCells[nextPosition.y][nextPosition.x].setPlayer(true);

        return Optional.of(new Board(newCells));
    }

    public Cell[][] getCells() {
        return cells;
    }

    private static Cell getCellAt(final Cell[][] cells, final int x, final int y) {
        return cells[y][x];
    }

    private static Cell getCellAt(final Cell[][] cells, final Position position) {
        return getCellAt(cells, position.x, position.y);
    }

    public Cell getCellAt(final int x, final int y) {
        return cells[y][x];
    }

    public Cell getCellAt(final Position position) {
        return getCellAt(position.x, position.y);
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public Set<Position> getBoxesPosition() {
        return boxesPosition;
    }

    public Board getSolutionBoard() {
        if (solutionBoard == null) {
            final Cell[][] newCells = Arrays.stream(cells)
                    .map(this::solveRow)
                    .toArray(Cell[][]::new);

            solutionBoard = new Board(newCells);
        }

        return solutionBoard;
    }

    private Cell[] solveRow(final Cell[] cells) {
        return Arrays.stream(cells)
                .map(this::solveCell)
                .toArray(Cell[]::new);
    }

    private Cell solveCell(final Cell cell) {
        final Cell newCell = cell.duplicate();

        if (newCell.isGoal()) {
            newCell.setBox(true);
        } else {
            newCell.setBox(false);
            newCell.setPlayer(false);
        }

        return newCell;
    }

    @Override
    public Board duplicate() {
        if (cells == null) return null;

        final Cell[][] newCells = Arrays.stream(cells).map(row ->
                Arrays.stream(row).map(Cell::duplicate).toArray(Cell[]::new)
        ).toArray(Cell[][]::new);

        return new Board(newCells);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (playerPosition != null ? !playerPosition.equals(board.playerPosition) : board.playerPosition != null)
            return false;
        return boxesPosition != null ? boxesPosition.equals(board.boxesPosition) : board.boxesPosition == null;
    }

    @Override
    public int hashCode() {
        int result = playerPosition != null ? playerPosition.hashCode() : 0;
        result = 31 * result + (boxesPosition != null ? boxesPosition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Arrays.stream(cells).map(row -> Arrays.stream(row)
                .map(Cell::toString)
                .reduce((s, s2) -> s + s2)
                .orElse("")
        ).reduce((s, s2) -> s + "\n" + s2)
                .orElse("");
    }
}
