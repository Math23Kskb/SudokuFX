package org.mck.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.mck.generator.BoardGenerator;
import org.mck.model.Board;

public class GameController {
    private final Board board;
    private final BoardGenerator boardGenerator;
    private SudokuBoardView boardView;

    public GameController(Board board, BoardGenerator boardGenerator) {
        this.board = board;
        this.boardGenerator = boardGenerator;
    }

    public void setBoardView(SudokuBoardView boardView) {
        this.boardView = boardView;

        initializeGame();
    }

    @FXML
    private GridPane boardGrid;

    private void initializeGame() {
        boardGenerator.generateBoard(board);
        boardView.initializeBoardDisplay();
    }

    @FXML
    public void handleCellInput(ActionEvent event) {
        TextField cellField = (TextField) event.getSource();
        Integer row = GridPane.getRowIndex(cellField);
        Integer col = GridPane.getColumnIndex(cellField);

        if (row == null || col == null) return;

        String text = cellField.getText();

        if (text != null && !text.isEmpty()) {

            if (!text.matches("[1-9]")) {
                showInvalidMoveAlert("Please enter digits from 1-9 only.");
                cellField.clear();
                return;
            }
            int userValue = Integer.parseInt(text);

            if (board.isValidMove(row, col, userValue)) {
                board.setValue(row, col, userValue);
            } else {
                showInvalidMoveAlert("Invalid move! This number violates Sudoku rules.");
                cellField.clear();
            }

            boardView.updateBoardDisplay();
        }
    }

    private void showInvalidMoveAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText("The move is not valid!");
        alert.setContentText(message);
        alert.showAndWait();
    }

/*
    @FXML
    public void handleReset() {
        board.resetBoard();
        initializeGame();
    }

    @FXML
    public void handleExit() {
        Stage stage = (Stage) boardGrid.getScene().getWindow();
        stage.close();
    }*/
}
