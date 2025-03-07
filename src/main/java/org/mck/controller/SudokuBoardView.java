package org.mck.controller;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.mck.model.Board;

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

        cellField.setOnAction(gameController::handleCellInput); // Delegate event handling back to controller

        return cellField;
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
