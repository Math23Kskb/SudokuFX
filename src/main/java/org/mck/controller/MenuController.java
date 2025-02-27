package org.mck.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuController {

    @FXML
    private Button startGameButton;

    @FXML
    public void startNewGame() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
        Scene gameScene = new Scene(loader.load());
        gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

        Stage stage = (Stage) startGameButton.getScene().getWindow();
        stage.setScene(gameScene);
        stage.show();
    }
}
