package com.example.steganography.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    
    public static void display(String title, String message) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        Label label = new Label(message);
        label.setTextAlignment(TextAlignment.CENTER);

        Button ok = new Button("OK");
        ok.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, ok);


        Scene scene = new Scene(layout, 250, 100);
        window.setScene(scene);
        window.showAndWait();
    }


}
