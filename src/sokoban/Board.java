package sokoban;

import model.Cell;

/**
 * Created by sebastian on 3/22/17.
 */
public class Board {

    private static Cell[][] board;

    public static void initialBoard(Cell[][] board) {
        Board.board = new Cell[board.length][board[0].length];
        for (int i = 0; i < board.length - 1; i++) {
            for (int j = 0; j < board.length - 1; j ++) {
                Board.board[i][j] = board[i][j].clone();
            }
        }
    }

    public static Cell[][] getBoard() {
        return board;
    }
}
