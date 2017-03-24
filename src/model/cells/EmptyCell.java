package model.cells;

public class EmptyCell extends AvailableCell {

    public EmptyCell(boolean hasPlayer, boolean hasBox) {
        super(hasPlayer, hasBox);
    }

    @Override
    public boolean isGoal() {
        return false;
    }

    @Override
    public String toString() {
        if (hasPlayer()) return "^";
        if (hasBox()) return "*";
        return "_";
    }


    @Override
    public Cell duplicate() {
        return new EmptyCell(hasPlayer(), hasBox());
    }
}
