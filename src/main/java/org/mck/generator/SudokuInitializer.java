package org.mck.generator;

import org.mck.model.Board;
import org.mck.model.SudokuBoard;
import org.mck.solver.PuzzleSolver;

import java.util.Random;

public class SudokuInitializer implements BoardInitializer {
    private final PuzzleSolver solver;

    public SudokuInitializer(PuzzleSolver solver) {
        this.solver = solver;
    }

    @Override
    public void initializeBoard(Board board) {
        fillDiagonal(board);
        solver.solve(board);
    }

    private void fillDiagonal(Board board) {
        Random rand = new Random();
        for (int i = 0; i < board.getSize(); i += board.getSubgridSize()) {
            boolean[] used = new boolean[board.getSize() + 1];
            for (int row = i; row < i + board.getSubgridSize(); row++) {
                for (int col = i; col < i + board.getSubgridSize(); col++) {
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
}
