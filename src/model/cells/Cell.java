package model.cells;

import model.Duplicable;

public interface Cell extends Duplicable<Cell> {

    boolean isWall();

    boolean isEmpty();

    boolean isGoal();

    boolean hasPlayer();

    boolean hasBox();

    void setPlayer(final boolean hasPlayer);

    void setBox(final boolean hasBox);

    /**
     * Factory method for {@code Cell}s.
     * Based on a char, it creates a new {@code Cell}.
     *
     * @param c char symbol that represents the {@code Cell}.
     * @return a new Cell.
     */
    static Cell valueOf(char c) {
        switch (c) {
            case 'w':
                return WallCell.getInstance();
            case '_':
                return new EmptyCell(false, false);
            case '*':
                return new EmptyCell(false, true);
            case '^':
                return new EmptyCell(true, false);
            case '°':
                return new GoalCell(false, false);
            case 'O':
                return new GoalCell(false, true);
            case '+':
                return new GoalCell(true, false);
            default:
                final String format = "Something went wrong when creating the cell from char %c";
                final String message = String.format(format, c);

                throw new RuntimeException(message);
        }
    }

}
