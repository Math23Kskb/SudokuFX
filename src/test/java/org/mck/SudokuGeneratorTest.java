package org.mck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mck.generator.SudokuGenerator;
import org.mck.generator.SudokuInitializer;
import org.mck.generator.SudokuRemover;
import org.mck.model.Board;
import org.mck.model.SudokuBoard;
import org.mck.solver.SudokuSolver;

import static org.junit.jupiter.api.Assertions.*;

class SudokuGeneratorTest {

    private SudokuGenerator generator;
    private Board board;


    @BeforeEach
    void setUp() {
        generator = new SudokuGenerator(new SudokuInitializer(new SudokuSolver()), new SudokuRemover(50));
        board = new SudokuBoard();

    }

    @Test
    void testGenerateBoard() {
        generator.generateBoard(board);
        int filledCells = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getValue(row, col) != 0) filledCells++;
            }
        }
        assertTrue(filledCells > 17);
    }
}
