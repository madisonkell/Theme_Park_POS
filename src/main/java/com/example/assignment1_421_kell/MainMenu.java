package com.example.assignment1_421_kell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 700);
        stage.setTitle("Jurassic Park Travel Limited");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}