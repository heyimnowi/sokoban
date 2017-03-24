package utils;

import model.cells.Cell;
import model.Board;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardReader {

    private int rows;
    private int cols;
    private Board board;

    public static Board fromFile(final String path) throws IOException {
        final List<String> lines = Files.readAllLines(Paths.get(path));
        final BoardReader reader = new BoardReader(lines);

        return reader.getBoard();
    }

    private BoardReader(final List<String> lines) {
        Objects.requireNonNull(lines);

        final String[] dimensions = lines.get(0).split(" ");
        this.rows = Integer.valueOf(dimensions[0]);
        this.cols = Integer.valueOf(dimensions[1]);

        final Cell[][] cells = this.populateCells(lines);
        this.board = new Board(cells);
    }

    private Cell[][] populateCells(final List<String> lines) {
        return lines.stream().skip(1)
                .map(this::parseRow)
                .toArray(Cell[][]::new);
    }

    private Cell[] parseRow(final String line) {
        // String#chars() throws an array of ints, yep...
        return line.chars()
                .mapToObj(this::parseCell)
                .toArray(Cell[]::new);
    }

    private Cell parseCell(final int c) {
        return Cell.valueOf((char) c);
    }

    private Board getBoard() {
        return board;
    }

}
