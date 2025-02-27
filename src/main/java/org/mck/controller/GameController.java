package org.mck.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.mck.model.SudokuBoard;
import org.mck.model.SudokuGenerator;

public class GameController {
    private final SudokuBoard board = new SudokuBoard();

    @FXML
    private GridPane boardGrid;

    @FXML
    public void initialize() {
        new SudokuGenerator(board);

        initializeBoard();
        updateBoardDisplay();
    }

    private void initializeBoard() {
        if (boardGrid == null) {
            throw new IllegalStateException("Grid is null");
        }

        boardGrid.getChildren().clear(); // Clear board

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = createCell(row, col);
                boardGrid.add(cell, col, row);
            }
        }
    }

    private TextField createCell(int row, int col) {
        TextField cellField = new TextField();
        cellField.setPrefSize(50, 50);
        cellField.setStyle("-fx-font-size: 18px; -fx-alignment: center;");

        int subgridIndex = getSubgridIndex(row, col);
        cellField.getStyleClass().add("subgrid-" + subgridIndex);

        populateCell(cellField, row, col);
        cellField.applyCss();

        cellField.setOnAction(this::handleCellInput);

        return cellField;
    }

    private int getSubgridIndex(int row, int col) {
        int subgridRow = row / 3;
        int subgridCol = col / 3;
        return subgridRow * 3 + subgridCol;
    }

    private void populateCell(TextField cellField, int row, int col) {
        int cellValue = board.getValue(row, col);
        if (cellValue != 0) {
            cellField.setText(String.valueOf(cellValue));
            cellField.setDisable(true);  // Lock initial numbers
        }
    }

    @FXML
    private void handleRealTimeValidation(KeyEvent event, int row, int col, TextField cellField) {
        String text = cellField.getText();

        // Check if the input is a valid number between 1 and 9
        if (text.matches("[1-9]")) {
            int userValue = Integer.parseInt(text);

            if (board.isValidMove(row, col, userValue)) {
                // If valid move, set the value in the board model and update UI
                board.setValue(row, col, userValue);
            } else if (!text.isEmpty()) {
                // If not a number, show an alert and clear the input
                showInvalidMoveAlert("Invalid input! Please enter a number between 1 and 9.");
                cellField.clear();  // Clear the invalid character
            }
        }
    }

    @FXML
    public void handleCellInput(ActionEvent event) {
        TextField cellField = (TextField) event.getSource();
        Integer row = GridPane.getRowIndex(cellField);
        Integer col = GridPane.getColumnIndex(cellField);

        String text = cellField.getText();

        if (text != null && !text.isEmpty()) {

            if (!text.matches("[1-9]")) {
                showInvalidMoveAlert("Test1");
                cellField.clear();
                return;
            }
            int userValue = Integer.parseInt(text);

            if (board.isValidMove(row, col, userValue)) {
                board.setValue(row, col, userValue);
            } else {
                showInvalidMoveAlert("Test2");
                cellField.clear();
            }

            updateBoardDisplay();
        }
    }

    private void updateBoardDisplay() {
        for (var node : boardGrid.getChildren()) {
            if (node instanceof TextField cellField) {
                Integer row = GridPane.getRowIndex(cellField);
                Integer col = GridPane.getColumnIndex(cellField);
                if (row == null || col == null) continue;

                int cellValue = board.getValue(row, col);
                cellField.setText(cellValue != 0 ? String.valueOf(cellValue) : "");

                int subgridIndex = getSubgridIndex(row, col);
                cellField.getStyleClass().removeIf(style -> style.startsWith("subgrid-"));
                cellField.getStyleClass().add("subgrid-" + subgridIndex);
            }
        }
    }

    private void showInvalidMoveAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText("The move is not valid!");
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void handleReset() {
        boardGrid.getChildren().clear();
        new SudokuGenerator(board);
        initializeBoard();
        updateBoardDisplay();
    }

    @FXML
    public void handleExit() {
        Stage stage = (Stage) boardGrid.getScene().getWindow();
        stage.close();
    }
}
