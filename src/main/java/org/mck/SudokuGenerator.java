package org.mck;

import java.util.Random;

public class SudokuGenerator {
    private final SudokuBoard board;
    private final SudokuSolver solver;

    public SudokuGenerator(SudokuBoard board) {
        this.board = board;
        this.solver = new SudokuSolver(board);
        generateBoard();
    }

    private void generateBoard() {
        fillDiagonal();
        solver.solve();
        removeNumbers(40);
    }

    private void fillDiagonal() {
        Random rand = new Random();
        for (int i = 0; i < SudokuBoard.SIZE; i += SudokuBoard.SUBGRID) {
            boolean[] used = new boolean[SudokuBoard.SIZE + 1];
            for (int row = i; row < i + SudokuBoard.SUBGRID; row++) {
                for (int col = i; col < i + SudokuBoard.SUBGRID; col++) {
                    int num;
                    do {
                        num = rand.nextInt(9) + 1;
                    } while (used[num]);
                    used[num] = true;
                    board.setValue(row, col, num);
                }
            }
        }
    }

    private void removeNumbers(int count) {
        Random rand = new Random();
        while (count > 0) {
            int row = rand.nextInt(SudokuBoard.SIZE);
            int col = rand.nextInt(SudokuBoard.SIZE);
            if (!board.isCellEmpty(row, col)) {
                board.setValue(row, col, 0);
                count--;
            }
        }
    }
}
