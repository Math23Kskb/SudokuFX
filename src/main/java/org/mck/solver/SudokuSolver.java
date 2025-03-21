package org.mck.solver;

import org.mck.model.Board;

public class SudokuSolver implements PuzzleSolver {

    private Board board;

    public SudokuSolver() {
    }

    @Override
    public boolean solve(Board board) {
        this.board = board;
        return solveRecursive(0, 0);
    }

    private boolean solveRecursive(int row, int col) {
        if (col == board.getSize()) {
            col = 0;
            row++;
            if (row == board.getSize()) return true;
        }
        if (!board.isCellEmpty(row, col)) return solveRecursive(row, col + 1);

        for (int num = 1; num <= board.getSize(); num++) {
            if (board.isValidMove(row, col, num)) {
                board.setValue(row, col, num);
                if (solveRecursive(row, col + 1)) return true;
                board.setValue(row, col, 0); // Backtrack
            }
        }
        return false;
    }
}
