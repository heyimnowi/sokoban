package sokoban;

import model.Cell;
import model.Position;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by sebastian on 3/22/17.
 */
public class SokobanProblem {



    public SokobanProblem() {
        startProblem();
    }

    private void startProblem() {

        String sCurrentLine;

        Scanner s;
        try {
            s = new Scanner(new File("src/boards/defaultBoard.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            return;
        }
        int rows = s.nextInt();
        int cols = s.nextInt();
        s.nextLine();
        Cell[][] map = new Cell[rows][cols];
        Position playerPosition = null;
        for (int i = 0; i < rows; i++) {
            sCurrentLine = s.nextLine();
            for (int j = 0; j < cols; j++) {
                char nextChar = sCurrentLine.charAt(j);
                System.out.print(nextChar);

                if (nextChar == '^' || nextChar=='+') {
                    playerPosition = new Position(i, j);
                }
                map[i][j] = Cell.getCell(nextChar);
            }
            System.out.println();
        }
        s.close();

        if (playerPosition == null) {
            System.err.println("Player position not found.");
        }
        Board.initialBoard(map);
    }

    public static void main(String[] args) {
        SokobanProblem problem = new SokobanProblem();
    }
}
