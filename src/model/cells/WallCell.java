package model.cells;

public class WallCell implements Cell {

    private static WallCell instance = new WallCell();

    public static Cell getInstance() {
        return instance;
    }

    @Override
    public boolean isWall() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isGoal() {
        return false;
    }

    @Override
    public boolean hasPlayer() {
        return false;
    }

    @Override
    public boolean hasBox() {
        return false;
    }

    @Override
    public void setPlayer(final boolean hasPlayer) {}

    @Override
    public void setBox(final boolean hasBox) {}

    @Override
    public String toString() {
        return "w";
    }

    @Override
    public Cell duplicate() {
        return WallCell.getInstance();
    }
}
