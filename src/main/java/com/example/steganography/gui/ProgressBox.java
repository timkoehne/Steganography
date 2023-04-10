package com.example.steganography.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProgressBox{

	Stage window;
	ProgressIndicator pi = new ProgressIndicator();
	
	
	public void display(String title, String message){
		
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		window.setOnCloseRequest(e -> {
			AlertBox.display("Error!", "Cancelling not possible");
		});
		
		
		Label label = new Label(message);
		label.setTextAlignment(TextAlignment.CENTER);
		
		
		VBox layout = new VBox(10);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(label, pi);
		
		
		Scene scene = new Scene(layout, 250, 150);
		window.setScene(scene);
		window.show();
	}

	public void beenden(){
		
		window.close();
		
	}
	
	
}
