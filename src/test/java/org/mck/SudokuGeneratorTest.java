package org.mck;

import org.junit.jupiter.api.Test;
import org.mck.model.SudokuBoard;
import org.mck.model.SudokuGenerator;

import static org.junit.jupiter.api.Assertions.*;

class SudokuGeneratorTest {

    @Test
    void testGenerateBoard() {
        SudokuBoard board = new SudokuBoard();
        new SudokuGenerator(board);

        int filledCells = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getValue(row, col) != 0) filledCells++;
            }
        }
        assertTrue(filledCells >= 17);
    }
}
