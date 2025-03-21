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
    void boardInitialization_CreatesEmptyBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                assertEquals(0, board.getValue(row, col), "Board should be empty initially");
            }
        }
    }

    @Test
    void setValueAndGetValue_SetsAndRetrievesValue() {
        board.setValue(0, 0, 5);
        assertEquals(5, board.getValue(0, 0));
    }

    @Test
    void isValidMove_ValidAndInvalidMoves_ReturnsCorrectly() {
        board.setValue(0, 0, 5);
        assertFalse(board.isValidMove(0, 1, 5), "Should be invalid due to row conflict");
        assertFalse(board.isValidMove(1, 0, 5), "Should be invalid due to column conflict");
        assertFalse(board.isValidMove(1, 1, 5), "Should be invalid due to box conflict");
        assertTrue(board.isValidMove(3, 3, 5), "Should be valid move");
    }

    @Test
    void isCellEmpty_EmptyAndNotEmptyCells_ReturnsCorrectly() {
        assertTrue(board.isCellEmpty(0, 0), "Cell should be empty initially");
        board.setValue(0, 0, 3);
        assertFalse(board.isCellEmpty(0, 0), "Cell should not be empty after setting value");
    }

    @Test
    void isNumberInRow_NumberPresentAndAbsent_ReturnsCorrectly() {
        board.setValue(0, 0, 5);

        assertTrue(board.isNumberInRow(0, 5), "Number 5 should be in row 0");
        assertFalse(board.isNumberInRow(0, 6), "Number 6 should not be in row 0");
    }

    @Test
    void isNumberInColumn_NumberPresentAndAbsent_ReturnsCorrectly() {
        board.setValue(0, 0, 5);

        assertTrue(board.isNumberInColumn(0, 5),"Number 5 should be in column 0");
        assertFalse(board.isNumberInColumn(0, 6), "Number 6 should not be in column 0");
    }

    @Test
    void isNumberInBox_NumberPresentAndAbsent_ReturnsCorrectly() {
        board.setValue(0, 0, 5);

        assertTrue(board.isNumberInBox(0, 0, 5), "Number 5 should be in box containing cell (0,0)");
        assertFalse(board.isNumberInBox(0, 0, 6), "Number 6 should not be in box containing cell (0,0)");
    }

    @Test
    void setValue_UpdatesInternalSetsCorrectly() {

        assertTrue(board.isCellEmpty(0,0), "Initial state: Cell should be empty");
        assertFalse(board.isNumberInRow(0,5), "Initial state: Row should not contain 5");
        assertFalse(board.isNumberInColumn(0,5), "Initial state: Column should not contain 5");
        assertFalse(board.isNumberInBox(0,0,5), "Initial state: Box should not contain 5");


        board.setValue(0, 0, 5);


        assertEquals(5, board.getValue(0, 0), "Value should be set to 5");
        assertTrue(board.isNumberInRow(0, 5), "Row should contain 5 after setting");
        assertTrue(board.isNumberInColumn(0, 5), "Column should contain 5 after setting");
        assertTrue(board.isNumberInBox(0, 0, 5), "Box should contain 5 after setting");

        board.setValue(0, 0, 0);

        assertEquals(0, board.getValue(0, 0), "Value should be reset to 0");
        assertFalse(board.isNumberInRow(0, 5), "Row should not contain 5 after reset");
        assertFalse(board.isNumberInColumn(0, 5), "Column should not contain 5 after reset");
        assertFalse(board.isNumberInBox(0, 0, 5), "Box should not contain 5 after reset");

        board.setValue(0, 0, 5);
        board.setValue(0, 0, 6);

        assertEquals(6, board.getValue(0, 0), "Value should be overwritten to 6");
        assertFalse(board.isNumberInRow(0, 5), "Row should not contain 5 after overwrite");
        assertTrue(board.isNumberInRow(0, 6), "Row should contain 6 after overwrite");
    }

    @Test
    void isBoardComplete_ValidCompleteBoard_ReturnsTrue() {
        SudokuBoard solvedBoard = createSolvedBoard();

        assertTrue(solvedBoard.isBoardComplete(), "Solved board should be complete");
    }

    @Test
    void isBoardComplete_IncompleteBoard_ReturnsFalse() {
        assertFalse(board.isBoardComplete(), "Empty board should be incomplete");

        board.setValue(0, 0, 5);

        assertFalse(board.isBoardComplete(),"Partially filled board should be incomplete");
    }

    @Test
    void testIsBoardComplete_CompleteInvalid() {
        SudokuBoard invalidBoard = createSolvedBoard();
        invalidBoard.setValue(0, 1, invalidBoard.getValue(0, 0));

        assertTrue(invalidBoard.isBoardComplete());
    }


    private SudokuBoard createSolvedBoard() {
        SudokuBoard solvedBoard = new SudokuBoard();
        int[][] solution = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                solvedBoard.setValue(row, col, solution[row][col]);
            }
        }
        return solvedBoard;
    }
}
