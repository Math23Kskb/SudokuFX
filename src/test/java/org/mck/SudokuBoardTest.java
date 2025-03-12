package org.mck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mck.model.SudokuBoard;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {

    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard();
    }

    @Test
    void testBoardInitialization() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                assertEquals(0, board.getValue(row, col), "Board should be empty initially");
            }
        }
    }

    @Test
    void testSetAndGetValue() {
        board.setValue(0, 0, 5);
        assertEquals(5, board.getValue(0, 0));
    }

    @Test
    void testIsValidMove() {
        board.setValue(0, 0, 5);
        assertFalse(board.isValidMove(0, 1, 5));
        assertFalse(board.isValidMove(1, 0, 5));
        assertFalse(board.isValidMove(1, 1, 5));
        assertTrue(board.isValidMove(3, 3, 5));
    }

    @Test
    void testIsCellEmpty() {
        assertTrue(board.isCellEmpty(0, 0));
        board.setValue(0, 0, 3);
        assertFalse(board.isCellEmpty(0, 0));
    }
}
