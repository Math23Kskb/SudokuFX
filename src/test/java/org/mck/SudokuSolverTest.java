package org.mck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mck.model.Board;
import org.mck.model.SudokuBoard;
import org.mck.solver.SudokuSolver;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuSolverTest {

    private SudokuBoard board;
    private SudokuSolver solver;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard();
        solver = new SudokuSolver();
    }

    @Test
    void testSolve() {
        board.setValue(0, 0, 5);
        solver.solve(board);
        assertBoardIsSolvedAndValid(board);

    }

    @Test
    void solve_WithInitialValues_SolvesBoardUsingBacktracking() {
        board.setValue(0,0,5);
        board.setValue(0,1,3);
        board.setValue(1,0,6);

        solver.solve(board);
        assertBoardIsSolvedAndValid(board);

    }

    private void assertBoardIsSolvedAndValid(Board board) {
        assertTrue(board.isBoardComplete(), "Board should be complete after solving");

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board.getValue(row, col);
                assertTrue(value >= 1 && value <= 9, "Invalid value at (" + row + "," + col + "): " + value);
            }
        }

        for (int row = 0; row < 9; row++) {
            Set<Integer> rowValues = new HashSet<>();
            for (int col = 0; col < 9; col++) {
                assertTrue(rowValues.add(board.getValue(row, col)), "Duplicate in row " + row);
            }
        }

        for (int col = 0; col < 9; col++) {
            Set<Integer> colValues = new HashSet<>();
            for (int row = 0; row < 9; row++) {
                assertTrue(colValues.add(board.getValue(row, col)), "Duplicate in column " + col);
            }
        }

        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                Set<Integer> boxValues = new HashSet<>();
                for (int row = boxRow * 3; row < boxRow * 3 + 3; row++) {
                    for (int col = boxCol * 3; col < boxCol * 3 + 3; col++) {
                        assertTrue(boxValues.add(board.getValue(row, col)), "Duplicate in 3x3 box at (" + boxRow + "," + boxCol + ")");
                    }
                }
            }
        }
    }
}