package org.mck.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mck.Main;
import org.mck.controller.GameController;
import org.mck.controller.SudokuBoardView;
import org.mck.generator.*;
import org.mck.model.Board;
import org.mck.model.SudokuBoard;
import org.mck.solver.SudokuSolver;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuSystemTest extends ApplicationTest {

    private GameController gameController;
    private Board solvedBoardInstance;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader gameLoader = new FXMLLoader(Main.class.getResource("/fxml/GameView.fxml"));
        Parent gameRoot = gameLoader.load(); // Load GameView.fxml FIRST
        GridPane boardGridFromFXML = (GridPane) gameRoot.lookup("#boardGrid"); // Get boardGrid from loaded FXML

        Board board = new SudokuBoard();
        SudokuInitializer initializer = new SudokuInitializer(new SudokuSolver());
        SudokuRemover remover = new SudokuRemover(5);

        BoardGenerator boardGenerator = new SudokuGenerator(initializer, remover) {
            @Override
            public void generateBoard(Board board) {
                initializer.initializeBoard(board);
                solvedBoardInstance = board.deepCopy();
                remover.removeNumbers(board);
            }
        };


        gameController = new GameController(board, boardGenerator);
        gameLoader.setController(gameController);
        gameController = gameLoader.getController();

        SudokuBoardView boardView = new SudokuBoardView(boardGridFromFXML, board, gameController);
        gameController.setBoardView(boardView);

        Scene gameScene = new Scene(gameRoot);
        gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

        stage.setTitle("Sudoku Game Test");
        stage.setScene(gameScene);
        stage.show();

    }

    @Test
    void startGameButton_Click_LoadsGameView() {
        assertNotNull(lookup("#boardGrid").query(), "Game View should be loaded with boardGrid");
    }

    private TextField getCell(int row, int col) {
        return lookup("#boardGrid").queryAs(GridPane.class).getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row
                        && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == col
                        && node instanceof TextField)
                .map(node -> (TextField) node)
                .findFirst()
                .orElse(null);
    }

    @Test
    void findAllEmptyCells_BoardIsGenerated_FindsEmptyCells() {
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Empty cells should be found");
    }

    @Test
    void enterValidNumberInFirstEmptyCell_DirectInput_NoAlert() {

        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");
        TextField firstEmptyCell = emptyCells.get(0);
        int row = GridPane.getRowIndex(firstEmptyCell);
        int col = GridPane.getColumnIndex(firstEmptyCell);

        Integer validNumberToEnter = null;  // Find a valid move
        for (int number = 1; number <= 9; number++) {
            if (gameController.isValidSudokuMove(row, col, number)) {
                validNumberToEnter = number;
                break;
            }
        }

        assertNotNull(validNumberToEnter, "Should be at least one valid move for the first empty cell");

        clickOn(firstEmptyCell);
        write(String.valueOf(validNumberToEnter));

        assertFalse(firstEmptyCell.getText().isEmpty(), "First empty cell should have a number after valid input");

    }

    @Test
    void enterInvalidNumberInRandomEmptyCell_ShowsAlert() {
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");

        TextField emptyCell = emptyCells.get(0);
        int row = GridPane.getRowIndex(emptyCell);
        int col = GridPane.getColumnIndex(emptyCell);

        int invalidNumber;
        if(findInvalidNumberForRow(row) >= 0){
            invalidNumber = findInvalidNumberForRow(row);
        }
        else if (findInvalidNumberForColumn(col) >= 0) {
            invalidNumber = findInvalidNumberForColumn(col);
        }
        else{
            invalidNumber = findInvalidNumberForBox(row, col);
        }

        clickOn(emptyCell);
        write(String.valueOf(invalidNumber));

        assertTrue(lookup(".alert").tryQuery().isPresent(), "Alert should be displayed for invalid move");

        clickOn("OK");
        assertFalse(lookup(".alert").tryQuery().isPresent(), "Alert should be closed after clicking OK");
        assertEquals("", emptyCell.getText(), "Cell should be cleared after invalid input");
    }

    @Test
    void CompleteBoard_ShowsGameOverWindow() {
        Board gameBoard = gameController.getBoard();
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");

        completeSudokuBoardForTesting();

        TextField lastCell = getCell(8, 8);
        assertTrue(gameBoard.isBoardComplete(), "Board should be complete");
        clickOn(lastCell);
        type(KeyCode.ENTER);

        assertNotNull(lookup("#startNewGameButton").query(), "GameOver window should be displayed");
    }

    @Test
    void startNewGameButton_FromGameOverWindow_StartsNewGame() {
        CompleteBoard_ShowsGameOverWindow();

        clickOn("#startNewGameButton");

        assertNotNull(lookup("#boardGrid").query(), "Game View should be loaded again for a new game");

        List<TextField> emptyCellsAfterNewGame = findAllEmptyCells();
        assertTrue(emptyCellsAfterNewGame.isEmpty(), "New game should have empty cells");
    }

    @Test
    void enterInvalidNumber_RowConflict_ShowsRowsConflictAlert() {
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");
        TextField emptyCellForRowConflict = emptyCells.get(0);
        int row = GridPane.getRowIndex(emptyCellForRowConflict);

        int invalidNumberForRowConflict = findInvalidNumberForRow(row);

        clickOn(emptyCellForRowConflict);
        write(String.valueOf(invalidNumberForRowConflict));


        assertTrue(lookup(".alert").tryQuery().isPresent(), "Alert for row conflict should be displayed");
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertTrue(dialogPane.getContentText().contains("already in row"), "Alert message should mention row conflict");
        clickOn("OK");
    }

    @Test
    void enterInvalidNumber_ColumnConflict_ShowsColumnsConflictAlert() {
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");
        TextField emptyCellForColumnConflict = emptyCells.get(0);
        int col = GridPane.getColumnIndex(emptyCellForColumnConflict);

        int invalidNumberForRowConflict = findInvalidNumberForColumn(col);

        clickOn(emptyCellForColumnConflict);
        write(String.valueOf(invalidNumberForRowConflict));


        assertTrue(lookup(".alert").tryQuery().isPresent(), "Alert for row conflict should be displayed");
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertTrue(dialogPane.getContentText().contains("already in column"), "Alert message should mention column conflict");
        clickOn("OK");
    }

    @Test
    void enterInvalidNumber_BoxConflict_ShowsBoxConflictsAlert() {
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");
        TextField emptyCellForBoxConflict = emptyCells.get(0);
        int row = GridPane.getRowIndex(emptyCellForBoxConflict);
        int col = GridPane.getColumnIndex(emptyCellForBoxConflict);

        int invalidNumberForBoxConflict = findInvalidNumberForBox(row, col);

        clickOn(emptyCellForBoxConflict);
        write(String.valueOf(invalidNumberForBoxConflict));


        assertTrue(lookup(".alert").tryQuery().isPresent(), "Alert for box conflict should be displayed");
        DialogPane dialogPane = lookup(".dialog-pane").queryAs(DialogPane.class);
        assertTrue(dialogPane.getContentText().contains("already in the 3x3 block"), "Alert message should mention box conflict");
        clickOn("OK");
    }



    // ====================== HELPER METHODS ====================== //

    private void completeSudokuBoardForTesting() {
        if (gameController == null) {
            System.out.println("ERROR: gameController  is NULL in completeSudokuBoardForTesting()!");
            return;
        }

        if (solvedBoardInstance == null) {
            System.out.println("ERROR: solvedBoardInstance is NULL in completeSudokuBoardForTesting()!");
            return;
        }

        Board gameBoard = gameController.getBoard();

        if (gameBoard == null) {
            System.out.println("ERROR: gameBoard (from gameController) is NULL in completeSudokuBoardForTesting()!");
            return;
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = getCell(row, col);
                if (cell != null && !cell.isDisable() && cell.getText().isEmpty()) {
                    int correctValue = solvedBoardInstance.getValue(row, col);
                    clickOn(cell);
                    write(String.valueOf(correctValue));
                }
            }
        }
    }

    private List<TextField> findAllEmptyCells() {
        List<TextField> emptyCells = new ArrayList<>();
        GridPane boardGridPane = lookup("#boardGrid").queryAs(GridPane.class);

        if (boardGridPane != null) {
            for (javafx.scene.Node node : boardGridPane.getChildren()) {
                if (node instanceof TextField cell) {
                    if (cell.getText().isEmpty() && !cell.isDisabled()) {
                        emptyCells.add(cell);
                    }
                }
            }
        }
        return emptyCells;
    }


    private int findInvalidNumberForRow(int row) {

        for (int num = 1; num <= 9; num++) {
            if (solvedBoardInstance.isNumberInRow(row, num)) {
                return num;
            }
        }
        return -1;
    }

    private int findInvalidNumberForColumn(int col) {

        for (int num = 1; num <= 9; num++) {
            if (solvedBoardInstance.isNumberInColumn(col, num)) {
                return num;
            }
        }
        return -1;
    }

    private int findInvalidNumberForBox(int row, int col) {

        for (int num = 1; num <= 9; num++) {
            if (solvedBoardInstance.isNumberInBox(row, col, num)) {
                return num;
            }
        }
        return -1;
    }
}