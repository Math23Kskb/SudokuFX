package org.mck;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {

    @Test
    void testSetAndGetValue() {
        SudokuBoard board = new SudokuBoard();
        board.setValue(0, 0, 5);
        assertEquals(5, board.getValue(0, 0));
    }

    @Test
    void testIsValidMove() {
        SudokuBoard board = new SudokuBoard();
        board.setValue(0, 0, 5);
        assertFalse(board.isValidMove(0, 1, 5));
        assertFalse(board.isValidMove(1, 0, 5));
        assertFalse(board.isValidMove(1, 1, 5));
        assertTrue(board.isValidMove(3, 3, 5));
    }

    @Test
    void testIsCellEmpty() {
        SudokuBoard board = new SudokuBoard();
        assertTrue(board.isCellEmpty(0, 0));
        board.setValue(0, 0, 3);
        assertFalse(board.isCellEmpty(0, 0));
    }
}
