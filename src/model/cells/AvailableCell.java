package model.cells;

public abstract class AvailableCell implements Cell {

    private boolean hasPlayer;
    private boolean hasBox;

    public AvailableCell(final boolean hasPlayer, final boolean hasBox) {
        this.hasPlayer = hasPlayer;
        this.hasBox = hasBox;
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return !hasPlayer() && !hasBox();
    }

    @Override
    public boolean hasPlayer() {
        return hasPlayer;
    }

    @Override
    public boolean hasBox() {
        return hasBox;
    }

    @Override
    public void setPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    @Override
    public void setBox(boolean hasBox) {
        this.hasBox = hasBox;
    }
}
