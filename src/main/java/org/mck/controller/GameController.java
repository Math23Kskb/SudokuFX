package org.mck.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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
            System.out.println("Grid is null. boardGrid is not initialized");
            return;
        }

        boardGrid.getChildren().clear();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cellField = new TextField();
                cellField.setPrefSize(40, 40);
                cellField.setStyle("-fx-font-size: 18px; -fx-alignment: center;");

                int cellValue = board.getValue(row, col);
                if (cellValue != 0) {
                    cellField.setText(String.valueOf(cellValue));
                    cellField.setDisable(true);  // Lock initial numbers
                }

                cellField.setOnAction(this::handleCellInput);
                boardGrid.add(cellField, col, row);
            }
        }
    }


    @FXML
    public void handleCellInput(ActionEvent event) {
        TextField cellField = (TextField) event.getSource();
        Integer row = GridPane.getRowIndex(cellField);
        Integer col = GridPane.getColumnIndex(cellField);

        if (row == null || col == null) return;

        String text = cellField.getText();

        if (text != null && !text.isEmpty()) {
            int userValue = Integer.parseInt(text);

            if (board.isValidMove(row, col, userValue)) {
                board.setValue(row, col, userValue);
            } else {
                showInvalidMoveAlert();
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
            }
        }
    }

    private void showInvalidMoveAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText("The move is not valid!");
        alert.setContentText("Please choose a valid number (1-9) and a valid position.");
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
