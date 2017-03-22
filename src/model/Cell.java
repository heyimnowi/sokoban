package model;

/**
 * Created by sebastian on 3/22/17.
 */
public class Cell {

    private boolean player, box;
    private final String type;

    /**
     * Representation of a board cell.
     *
     * @param box Boolean. Tells if the cell has a box on it.
     * @param player Boolean. Tells if the cell has the player on it.
     * @param type String. Tells if the cell contains Wall or a Goal, or it's Available
     *
     */
    public Cell (String type, boolean player, boolean box) {
        this.box = box;
        this.player = player;
        this.type = type;
    }

    public boolean isWall() {
        return type.equals(new String("Wall"));
    }

    public boolean isEmpty() {
        return !type.equals(new String("Wall")) && !player && !box;
    }

    public boolean isGoal() {
        return type.equals(new String("Goal"));
    }

    /**
     * Representation of moving. If both params are true, it wont work.
     *
     * @param box Boolean. Whether the cell is filled with a box.
     * @param player Boolean. Whether the cell filled with the player.
     */
    public void changeCell(boolean player, boolean box) {
        this.player = player;
        this.box = box;
    }

    public String toString() {
        switch (type) {
            case "Wall":
                return "w";
            case "Available":
                if (box) return "*";
                if (player) return "^";
                return "_";
            case "Goal":
                if (box) return "O";
                if (player) return "+";
                return "°";
        }
        throw new RuntimeException("Something went wrong when casting the cell to string");
    }

    public Cell clone() {
        return new Cell(type, player, box);
    }

    // Static Methods
    public static Cell getCell(char cellChar) {
        switch (cellChar) {
            case 'w':
                return new Cell("Wall", false, false);
            case '_':
                return new Cell("Available", false, false);
            case '*':
                return new Cell("Available", false, true);
            case '^':
                return new Cell("Available", true, false);
            case '°':
                return new Cell("Goal", false, false);
            case 'O':
                return new Cell("Goal", false, true);
            case '+':
                return new Cell("Goal", true, false);
        }
        throw new RuntimeException("Something went wrong when creating the cell from a char");
    }
}
