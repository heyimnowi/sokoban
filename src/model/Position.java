package model;

/**
 * Created by sebastian on 3/22/17.
 */
public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(final Position position) {
        this(position.x, position.y);
    }

    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return String.format("{ x: %d, y: %d }", x, y);
    }
}
