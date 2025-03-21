package org.mck.controller;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import org.mck.model.Board;

import java.io.IOException;

import static org.mck.model.SudokuBoard.SUBGRID;

public class SudokuBoardView {

    private GridPane boardGrid;
    private Board board;
    private GameController gameController;

    public SudokuBoardView(GridPane boardGrid, Board board,GameController gameController) {
        this.boardGrid = boardGrid;
        this.board = board;
        this.gameController = gameController;
    }

    public void initializeBoardDisplay() {
        if (boardGrid == null) {
            System.err.println("Warning: boardGrid is null in updateBoardDisplay().");
            return;
        }

        boardGrid.getChildren().clear();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = createCell(row, col);
                boardGrid.add(cell, col, row);
            }
        }
        updateBoardDisplay();
    }

    private TextField createCell(int row, int col) {
        TextField cellField = new TextField();
        cellField.setPrefSize(50, 50);
        cellField.setStyle("-fx-font-size: 18px; -fx-alignment: center;");

        int subgridIndex = getSubgridIndex(row, col);
        cellField.getStyleClass().add("subgrid-" + subgridIndex);

        populateCell(cellField, row, col);
        cellField.applyCss();

        TextFormatter<Integer> textFormatter = createTextFormatter();

        cellField.setTextFormatter(textFormatter);


        cellField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleCellInput(cellField, row, col, oldValue, newValue);
            });

        cellField.setOnAction(actionEvent -> {
            try {
                gameController.GameOver(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return cellField;
    }

    private TextFormatter<Integer> createTextFormatter() {
        return new TextFormatter<>(new IntegerStringConverter(), null, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]?")) {
                if (newText.isEmpty() || (newText.equals("0") && change.isDeleted())) {
                    return change;
                } else if (newText.matches("[1-9]")) {
                    return change;
                }
            }
            return null;
        });
    }

    private void handleCellInput(TextField cellField, int row, int col, String oldValue,String newValue) {
        if (!cellField.isFocused()) return;

        if (newValue.isEmpty()) {
            board.setValue(row, col, 0);
            updateBoardDisplay();
            return;
        }

        int userValue = Integer.parseInt(newValue);

        if (gameController.isValidSudokuMove(row, col, userValue)) {
            board.setValue(row, col, userValue);
            updateBoardDisplay();
        } else {
            gameController.showInvalidMoveAlert(userValue, row, col);
            cellField.setText(oldValue);
        }

    }

    private int getSubgridIndex(int row, int col) {
        int subgridRow = row / SUBGRID;
        int subgridCol = col / SUBGRID;
        return subgridRow * SUBGRID + subgridCol;
    }

    private void populateCell(TextField cellField, int row, int col) {
        int cellValue = board.getValue(row, col);
        if (cellValue != 0) {
            cellField.setText(String.valueOf(cellValue));
            cellField.setDisable(true); // Lock initial numbers
        }
    }

    public void updateBoardDisplay() {
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


}
