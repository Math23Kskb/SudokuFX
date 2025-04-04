package org.mck;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader menuLoader = new FXMLLoader(Main.class.getResource("/fxml/MainView.fxml"));
        VBox menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        menuScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

        primaryStage.setTitle("Sudoku Game");
        primaryStage.setScene(menuScene);
        primaryStage.show();

    }

    public static void main(String[] args)  {
        launch(args);
    }
}
