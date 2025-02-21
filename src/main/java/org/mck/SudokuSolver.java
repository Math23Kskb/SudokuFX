package org.mck;

public class SudokuSolver {

    private final SudokuBoard board;

    public SudokuSolver(SudokuBoard board) {
        this.board = board;
    }

    public void solve() {
        solveRecursive(0, 0);
    }

    private boolean solveRecursive(int row, int col) {
        if (col == SudokuBoard.SIZE) {
            col = 0;
            row++;
            if (row == SudokuBoard.SIZE) return true;
        }

        if (!board.isCellEmpty(row, col)) return solveRecursive(row, col + 1);

        for (int num = 1; num <= SudokuBoard.SIZE; num++) {
            if (board.isValidMove(row, col, num)) {
                board.setValue(row, col, num);
                if (solveRecursive(row, col + 1)) return true;
                board.setValue(row, col, 0); // Backtrack
            }
        }
        return false;
    }
}
