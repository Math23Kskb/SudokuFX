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
import org.mck.generator.*;
import org.mck.model.Board;
import org.mck.model.SudokuBoard;
import org.mck.solver.SudokuSolver;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuSystemTest extends ApplicationTest {

    private Stage primaryStage;
    private GameController gameController;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        FXMLLoader menuLoader = new FXMLLoader(Main.class.getResource("/fxml/MainView.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        menuScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

        stage.setTitle("Sudoku Game");
        stage.setScene(menuScene);
        stage.show();

        clickOn("#startGameButton");

        FXMLLoader gameLoader = new FXMLLoader(Main.class.getResource("/fxml/GameView.fxml"));
        Board board = new SudokuBoard();

        BoardGenerator boardGenerator = new SudokuGenerator(
                new SudokuInitializer(new SudokuSolver()), new SudokuRemover(1)
        );
        gameController = new GameController(board, boardGenerator);
        gameLoader.setController(gameController);
        Parent gameRoot = gameLoader.load();
        gameController = gameLoader.getController();
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

    private static class MoveSets {

        Set<Integer> validMoves;
        Set<Integer> usedMoves;
        public MoveSets(Set<Integer> validMoves, Set<Integer> usedMoves) {
            this.validMoves = validMoves;
            this.usedMoves = usedMoves;
        }

        public Set<Integer> getValidMoves() {
            return validMoves;
        }

        public Set<Integer> getUsedMoves() {
            return usedMoves;
        }

    }

    @Test
    void enterValidNumberInFirstEmptyCell_DirectInput_NoAlert() {

        List<TextField> emptyCells = findAllEmptyCells();

        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");
        TextField firstEmptyCell = emptyCells.get(0);
        int row = GridPane.getRowIndex(firstEmptyCell);
        int col = GridPane.getColumnIndex(firstEmptyCell);

        MoveSets moveSets = getValidAndUsedMovesForCell(row, col);
        Set<Integer> validMoves = moveSets.getValidMoves();

        assertFalse(validMoves.isEmpty(), "Should be at least one valid move for the first empty cell");
        Integer validNumberToEnter = validMoves.iterator().next();


        clickOn(firstEmptyCell);
        write(String.valueOf(validNumberToEnter));


        assertFalse(firstEmptyCell.getText().isEmpty(), "First empty cell should have a number after valid input");

    }


    @Test
    void enterInvalidNumberInRandomEmptyCell_ShowsAlert() {
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");
        TextField EmptyCell = emptyCells.get(0);

        clickOn(EmptyCell);
        write("9");

        assertTrue(lookup(".alert").tryQuery().isPresent(), "Alert should be displayed for invalid move");
        clickOn("OK");
        assertFalse(lookup(".alert").tryQuery().isPresent(), "Alert should be closed after clicking OK");
        assertEquals("", EmptyCell.getText(), "Cell should be cleared after invalid input");
    }


    @Test
    void GameOver_InvalidInputThenCompleteBoard_ShowsGameOverWindow() {
        TextField cellForInvalidInput = getCell(0, 0);
        assertNotNull(cellForInvalidInput, "Cell at 0,0 should exist");


        clickOn(cellForInvalidInput);
        write("9"); // Invalid input
        assertTrue(lookup(".alert").tryQuery().isPresent(), "Alert should be displayed for invalid move");
        clickOn("OK");
        assertFalse(lookup(".alert").tryQuery().isPresent(), "Alert should be closed after clicking OK");


        completeSudokuBoardForTesting();
        TextField lastCell = getCell(8, 8);
        assertNotNull(lastCell, "Last cell (8,8) should exist");
        clickOn(lastCell);
        type(KeyCode.ENTER);


        assertNotNull(lookup("#startNewGameButton").query(), "GameOver window should be displayed");
    }

    @Test
    void startNewGameButton_FromGameOverWindow_StartsNewGame() {
        GameOver_InvalidInputThenCompleteBoard_ShowsGameOverWindow();

        clickOn("#startNewGameButton");

        assertNotNull(lookup("#boardGrid").query(), "Game View should be loaded again for a new game");
    }

    @Test
    void enterValidNumberInAllEmptyCells_RecursiveInput_NoAlert() {
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");
        TextField firstEmptyCell = emptyCells.get(0);

        recursivelyEnterValidNumber(firstEmptyCell);

        assertFalse(firstEmptyCell.getText().isEmpty(), "First empty cell should have a number after recursive input");
        assertFalse(lookup(".alert").tryQuery().isPresent(), "No alert should be present after valid input");
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

    @Test
    void enterCorrectNumberInAllEmptyCells_RecursiveInput() {
        List<TextField> emptyCells = findAllEmptyCells();
        assertFalse(emptyCells.isEmpty(), "Should be at least one empty cell");

        for (TextField emptyCell : emptyCells) {
            recursivelyEnterValidNumber(emptyCell);
        }

        for (TextField emptyCell : emptyCells) {
            assertFalse(emptyCell.getText().isEmpty(), "Empty cell should have a number after recursive input");
            assertFalse(lookup(".alert").tryQuery().isPresent(), "No alert should be present after valid input");

        }

    }


    // ====================== HELPER METHODS ====================== //
    private MoveSets getValidAndUsedMovesForCell(int row, int col) {
        Set<Integer> validMoveSet = new HashSet<>();
        Set<Integer> usedMoveSet = new HashSet<>();

        for (int number = 1; number <= 9; number++) {
            if (gameController.isValidSudokuMove(row, col, number)) {
                validMoveSet.add(number);
            } else {
                usedMoveSet.add(number);
            }
        }
        return new MoveSets(validMoveSet, usedMoveSet);
    }

    private Board getSolvedBoard() {
        Board solvedBoard = new SudokuBoard();
        BoardGenerator solvedBoardGenerator = new SudokuGenerator(
                new SudokuInitializer(new SudokuSolver()), new SudokuRemover(0)
        );
        solvedBoardGenerator.generateBoard(solvedBoard);
        return solvedBoard;
    }
    private void completeSudokuBoardForTesting() {
        if (gameController == null) {
            System.out.println("ERROR: gameController is NULL in completeSudokuBoardForTesting()!");
            return; // Exit to avoid NullPointerException
        }
        solveBoardProgrammatically();

        Board board = gameController.getBoard();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int cellValue = board.getValue(row, col);
                TextField cell = getCell(row, col);
                if (cell != null && !cell.isDisable() && (cell.getText().isEmpty() || !cell.getText().equals(String.valueOf(cellValue)))) {
                    recursivelyEnterValidNumber(cell);
                }
            }
        }
    }

    private List<TextField> findAllEmptyCells() {
        List<TextField> emptyCells = new ArrayList<>();
        GridPane boardGridPane = lookup("#boardGrid").queryAs(GridPane.class);
        System.out.println("boardGridPane is null: " + (boardGridPane == null));
        if (boardGridPane != null) {
            System.out.println("Number of children in boardGridPane: " + boardGridPane.getChildren().size());
            for (javafx.scene.Node node : boardGridPane.getChildren()) {
                System.out.println("Node class: " + node.getClass().getSimpleName());
                if (node instanceof TextField) {
                    TextField cell = (TextField) node;
                    System.out.println("TextField found - Row: " + GridPane.getRowIndex(node) + ", Col: " + GridPane.getColumnIndex(node) + ", Text: '" + cell.getText() + "', Disabled: " + cell.isDisabled()); // Log TextField details
                    if (cell.getText().isEmpty() && !cell.isDisabled()) {
                        emptyCells.add(cell);
                    }
                }
            }
        }
        System.out.println("Number of empty cells to be returned: " + emptyCells.size()); // Log emptyCells size
        return emptyCells;
    }


    private int findInvalidNumberForRow(int row) {
        Board solvedBoard = getSolvedBoard();
        System.out.println("findInvalidNumberForRow: Row = " + row);

        for (int num = 1; num <= 9; num++) {
            if (solvedBoard.isNumberInRow(row, num)) {
                System.out.println("findInvalidNumberForRow: Returning invalid number (row conflict) = " + num);
                return num;
            }
        }
        System.out.println("findInvalidNumberForRow: No invalid number found (correct - should find one)");
        return -1;
    }

    private int findInvalidNumberForColumn(int col) {
        Board solvedBoard = getSolvedBoard();
        System.out.println("findInvalidNumberForColumn: Column = " + col);

        for (int num = 1; num <= 9; num++) {
            if (solvedBoard.isNumberInColumn(col, num)) {
                System.out.println("findInvalidNumberForColumn: Returning invalid number (column conflict) = " + num);
                return num;
            }
        }
        System.out.println("findInvalidNumberForColumn: No invalid number found (correct - should find one)");
        return -1;
    }

    private int findInvalidNumberForBox(int row, int col) {
        Board solvedBoard = getSolvedBoard();
        System.out.println("findInvalidNumberForBox: Row = " + row + ", Col = " + col);

        for (int num = 1; num <= 9; num++) {
            if (solvedBoard.isNumberInBox(row, col, num)) {
                System.out.println("findInvalidNumberForBox: Returning invalid number (box conflict) = " + num);
                return num;
            }
        }
        System.out.println("findInvalidNumberForBox: No invalid number found (correct - should find one)");
        return -1;
    }

    private void solveBoardProgrammatically() {
        Board board = gameController.getBoard();
        SudokuSolver solver = new SudokuSolver();
        boolean solved = solver.solve(board);
        assertTrue(solved, "Board should be solvable by the solver");
        assertTrue(board.isBoardComplete(), "Board should be complete after solving");
    }

    private void recursivelyEnterValidNumber(TextField cell) {
        if (cell == null) return;

        if (!cell.getText().isEmpty()) return;

        for (int number = 1; number <= 9; number++) {
            clickOn(cell);
            write(String.valueOf(number));
            if (lookup(".alert").tryQuery().isEmpty()) {
                return;
            } else {
                clickOn("OK");
                eraseText(1);
            }
        }
        fail("No valid number found for cell after trying 1-9");
    }

}