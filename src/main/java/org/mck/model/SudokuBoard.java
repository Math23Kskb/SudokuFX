package org.mck.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mck.util.BoardTemplate.BOARD_TEMPLATE;

public class SudokuBoard {

    public static final int SIZE = 9;
    public static final int SUBGRID = 3;

    private final List<Set<Integer>> rows;
    private final List<Set<Integer>> columns;
    private final List<Set<Integer>> boxes;

    private final int[][] board;

    public SudokuBoard() {
        board = new int[SIZE][SIZE];
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        boxes = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            rows.add(new HashSet<>());
            columns.add(new HashSet<>());
            boxes.add(new HashSet<>());
        }
    }

    public boolean isValidMove(int row, int col, int num) {
        int boxIndex = (row / SUBGRID) * SUBGRID + col / SUBGRID;
        return !rows.get(row).contains(num) &&
                !columns.get(col).contains(num) &&
                !boxes.get(boxIndex).contains(num);
    }

    public int getValue(int row, int col) {
        return board[row][col];
    }

    public void setValue(int row, int col, int num) {

        int boxIndex = (row / SUBGRID) * SUBGRID + col / SUBGRID;

        int prevNum = board[row][col];
        if (prevNum != 0) {
            rows.get(row).remove(prevNum);
            columns.get(col).remove(prevNum);
            boxes.get(boxIndex).remove(prevNum);
        }

        board[row][col] = num;
        if (num != 0) {
            rows.get(row).add(num);
            columns.get(col).add(num);
            boxes.get(boxIndex).add(num);
        }
    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col] == 0;
    }

    public void printBoard() {
        String[] values = new String[81];
        int index = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                values[index++] = (board[row][col] == 0) ? " " : String.format("%2d", board[row][col]);
            }
        }
        System.out.printf(BOARD_TEMPLATE, (Object[]) values);
    }

}
