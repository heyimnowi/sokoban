package model.cells;

public class GoalCell extends AvailableCell {

    public GoalCell(boolean hasPlayer, boolean hasBox) {
        super(hasPlayer, hasBox);
    }

    @Override
    public boolean isGoal() {
        return true;
    }

    @Override
    public String toString() {
        if (hasPlayer()) return "+";
        if (hasBox()) return "O";
        return "°";
    }

    @Override
    public Cell duplicate() {
        return new GoalCell(hasPlayer(), hasBox());
    }
}
