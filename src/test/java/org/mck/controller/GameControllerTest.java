package org.mck.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mck.generator.BoardGenerator;
import org.mck.model.Board;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    private GameController gameController;

    @Mock
    private Board board;
    @Mock
    private BoardGenerator boardGenerator;
    @Mock
    private SudokuBoardView boardView;
    @Mock
    private Stage mockStage;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(board, boardGenerator);
        gameController.setBoardView(boardView);
    }

    @Test
    void setBoardView_SetsBoardViewCorrectly() {

        SudokuBoardView newBoardView = mock(SudokuBoardView.class);

        gameController.setBoardView(newBoardView);

        assertEquals(newBoardView, gameController.getBoardView(), "BoardView should be set correctly");
    }

    @Test
    void isValidSudokuMove_WithValidMove_ReturnsTrue() {

        when(board.isValidMove(0, 0, 5)).thenReturn(true);

        assertTrue(gameController.isValidSudokuMove(0, 0, 5));
    }

    @Test
    void isValidSudokuMove_WithInvalidMove_RowConflict_ReturnsFalse() {

        when(board.isValidMove(0, 0, 5)).thenReturn(false);
        assertFalse(gameController.isValidSudokuMove(0, 0, 5));
    }

    @Test
    void isValidSudokuMove_WithInvalidMove_ColumnConflict_ReturnsFalse() {

        when(board.isValidMove(0, 0, 5)).thenReturn(false);
        assertFalse(gameController.isValidSudokuMove(0, 0, 5));
    }

    @Test
    void isValidSudokuMove_WithInvalidMove_BoxConflict_ReturnsFalse() {

        when(board.isValidMove(0, 0, 5)).thenReturn(false);
        assertFalse(gameController.isValidSudokuMove(0, 0, 5));
    }

    @Test
    void gameOver_WhenBoardIsComplete_ShowsGameOverWindow() throws IOException {

        when(board.isBoardComplete()).thenReturn(true);
        ActionEvent actionEvent = mock(ActionEvent.class);

        GameController gameControllerSpy = spy(gameController);
        Mockito.doNothing().when(gameControllerSpy).showGameOverWindow(any());

        gameControllerSpy.GameOver(actionEvent);

        verify(gameControllerSpy).showGameOverWindow(actionEvent);
        verify(board).isBoardComplete();
    }

    @Test
    void gameOver_WhenBoardIsNotComplete_DoesNotShowGameOverWindow() throws IOException {

        when(board.isBoardComplete()).thenReturn(false);
        ActionEvent actionEvent = mock(ActionEvent.class);
        GameController gameControllerSpy = spy(gameController);

        gameControllerSpy.GameOver(actionEvent);

        verify(gameControllerSpy, never()).showGameOverWindow(any());
        verify(board).isBoardComplete();

    }
}