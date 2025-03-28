package org.mck.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SudokuBoard implements Board{

    public static final int SIZE = 9;
    public static final int SUBGRID = 3;

    private final List<Set<Integer>> rows;
    private final List<Set<Integer>> columns;
    private final List<Set<Integer>> boxes;

    private final int[][] board;


    public boolean isNumberInRow(int row, int num) {
        return rows.get(row).contains(num);
    }

    public boolean isNumberInColumn(int col, int num) {
        return columns.get(col).contains(num);
    }

    public boolean isNumberInBox(int row, int col, int num) {
        int boxIndex = (row / SUBGRID) * SUBGRID + col / SUBGRID;
        return boxes.get(boxIndex).contains(num);
    }

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

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public int getSubgridSize() {
        return SUBGRID;
    }

    @Override
    public boolean isValidMove(int row, int col, int num) {
        int boxIndex = (row / SUBGRID) * SUBGRID + col / SUBGRID;
        return !rows.get(row).contains(num) &&
                !columns.get(col).contains(num) &&
                !boxes.get(boxIndex).contains(num);
    }

    @Override
    public int getValue(int row, int col) {
        return board[row][col];
    }

    @Override
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

    @Override
    public boolean isCellEmpty(int row, int col) {
        return board[row][col] == 0;
    }

    @Override
    public Board deepCopy() {
        SudokuBoard solvedBoardCopy = new SudokuBoard();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                solvedBoardCopy.setValue(i, j, this.board[i][j]);
            }
        }
        return solvedBoardCopy;
    }

    public boolean isBoardComplete() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    System.out.println("Board is NOT complete - empty cell at row " + row + ", col " + col); // ADD THIS LINE
                    return false;
                }
                int num = board[row][col];

                rows.get(row).remove(num);
                columns.get(col).remove(num);
                boxes.get((row / SUBGRID) * SUBGRID + col / SUBGRID).remove(num);

                if (!isValidMove(row, col, num)) {

                    rows.get(row).add(num);
                    columns.get(col).add(num);
                    boxes.get((row / SUBGRID) * SUBGRID + col / SUBGRID).add(num);
                    System.out.println("Board is NOT complete - invalid move at row " + row + ", col " + col + ", num " + num); // ADD THIS LINE
                    return false;
                }

                rows.get(row).add(num);
                columns.get(col).add(num);
                boxes.get((row / SUBGRID) * SUBGRID + col / SUBGRID).add(num);
            }
        }
        return true;
    }

}
