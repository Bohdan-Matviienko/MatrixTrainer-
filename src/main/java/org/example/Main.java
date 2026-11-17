package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/form.fxml")));
        Scene scene = new Scene(root, 980, 620);

        primaryStage.setTitle("MatrixTrainer — Множення та Додавання матриць");
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(540);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}