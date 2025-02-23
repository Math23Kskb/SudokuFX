package org.mck.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button startGameButton;

    @FXML
    public void startNewGame() throws IOException {
        Stage stage = (Stage) startGameButton.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
        Parent gameRoot = loader.load();

        stage.setScene(new Scene(gameRoot));
        stage.setTitle("Sudoku Game - Play");
        stage.show();
    }
}
