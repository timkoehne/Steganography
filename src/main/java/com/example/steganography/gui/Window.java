package com.example.steganography.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Steganography");

        MainLayout mainLayout = new MainLayout();
        Scene mainScene = new Scene(mainLayout, 600, 400);

        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

}
