package org.mck;

import org.junit.jupiter.api.Test;
import org.mck.model.SudokuBoard;
import org.mck.model.SudokuSolver;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuSolverTest {

    @Test
    void testSolve() {
        SudokuBoard board = new SudokuBoard();
        SudokuSolver solver = new SudokuSolver(board);

        board.setValue(0, 0, 5);
        solver.solve();


        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board.getValue(row, col);
                assertTrue(value >= 1 && value <= 9, "Invalid value at (" + row + "," + col + "): " + value);
            }
        }


        for (int row = 0; row < 9; row++) {
            Set<Integer> seen = new HashSet<>();
            for (int col = 0; col < 9; col++) {
                assertTrue(seen.add(board.getValue(row, col)), "Duplicate in row " + row);
            }
        }


        for (int col = 0; col < 9; col++) {
            Set<Integer> seen = new HashSet<>();
            for (int row = 0; row < 9; row++) {
                assertTrue(seen.add(board.getValue(row, col)), "Duplicate in column " + col);
            }
        }


        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                Set<Integer> seen = new HashSet<>();
                for (int row = boxRow * 3; row < boxRow * 3 + 3; row++) {
                    for (int col = boxCol * 3; col < boxCol * 3 + 3; col++) {
                        assertTrue(seen.add(board.getValue(row, col)), "Duplicate in 3x3 box at (" + boxRow + "," + boxCol + ")");
                    }
                }
            }
        }
    }


    @Test
    void testBackTrackingSolvesCorrectly() {
        SudokuBoard board = new SudokuBoard();
        board.setValue(0,0,5);
        board.setValue(0,1,3);
        board.setValue(1,0,6);

        SudokuSolver solver = new SudokuSolver(board);
        solver.solve();

    }
}