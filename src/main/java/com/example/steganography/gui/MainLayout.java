package com.example.steganography.gui;

import com.example.steganography.processing.Processing;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainLayout extends TabPane {

    boolean imageChanged = false;
    File f;
    TextArea eingabe;
    TextArea ausgabe;
    int anzPixel = 0;

    public MainLayout() {

        Tab tab0 = new Tab("Save in Image");
        tab0.setClosable(false);

        BorderPane tab0Layout = new BorderPane();

        tab0.setContent(tab0Layout);

        // Tab0 Links
        VBox links = new VBox(5);
        links.setPadding(new Insets(5));
        links.setSpacing(5);
        links.setAlignment(Pos.CENTER);
        Label labelAnz = new Label();

        ImageView iv = new ImageView();
        iv.setFitHeight(300);
        iv.setFitWidth(300);
        try {
            iv.setImage(SwingFXUtils
                    .toFXImage(ImageIO.read(new File(System.getProperty("user.dir") + "\\res\\pic0.png")), null));
        } catch (IOException e2) {
        }
        iv.setPreserveRatio(true);
        iv.setOnMouseClicked(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select image to save into...");
            fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
            fc.getExtensionFilters().addAll(new ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            f = fc.showOpenDialog(null);
            if (f != null) {
                if (f.isFile()) {
                    Image img = new Image(f.toURI().toString());
                    iv.setImage(img);
                    imageChanged = true;
                    anzPixel = Processing.numberUsablePixels(SwingFXUtils.fromFXImage(iv.getImage(), null));
                    labelAnz.setText((anzPixel / 3) - 1 - eingabe.getText().length()
                            + " characters can be saved");

                }
            }
        });

        tab0Layout.setLeft(links);
        links.getChildren().addAll(iv, labelAnz);

        // Tab0 Mitte
        VBox middle = new VBox(5);
        middle.setPadding(new Insets(5));
        middle.setSpacing(5);
        middle.setAlignment(Pos.CENTER);
        eingabe = new TextArea();
        eingabe.setWrapText(true);
        eingabe.setPromptText("Which text do you want to encode");
        Label labelAnzEingabe = new Label(eingabe.getText().length() + " characters entered");
        middle.getChildren().addAll(eingabe, labelAnzEingabe);
        tab0Layout.setCenter(middle);
        eingabe.textProperty().addListener(e -> {
            labelAnzEingabe.setText(eingabe.getText().length() + " characters entered");
            labelAnz.setText((anzPixel / 3) - 1 - ((eingabe.getText().length()) + "").length()
                    + " characters can be saved");
        });

        // Tab0 Rechts
        VBox rechts = new VBox();
        rechts.setPadding(new Insets(5));
        rechts.setSpacing(5);
        rechts.setAlignment(Pos.CENTER);

        Button speichern = new Button("Save");
        speichern.setOnAction(e -> {

            if (!labelAnz.getText().equals("")) {

                if (labelAnzEingabe.getText().substring(0, labelAnzEingabe.getText().indexOf(" ")).equals("0")) {
                    AlertBox.display("Error!", "You need to enter a message!");
                } else {
                    if (Integer.parseInt(labelAnz.getText().substring(0, labelAnz.getText().indexOf(" "))) >= eingabe
                            .getText().length()) {

                        try {// Bild speichern
                            FileChooser fc = new FileChooser();
                            fc.setTitle("Where do you want to save the result?");
                            fc.setInitialFileName("image.png");
                            fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
                            fc.getExtensionFilters().addAll(new ExtensionFilter("Images", "*.png"));
                            File newFile = fc.showSaveDialog(null);
                            if (newFile != null) {
                                if (!newFile.exists())
                                    newFile.createNewFile();

                                ProgressBox pb = new ProgressBox();
                                pb.display("Loading...", "Saving...");
                                new Thread() {
                                    public void run() {
                                        Processing.saveInImage(SwingFXUtils.fromFXImage(iv.getImage(), null),
                                                eingabe.getText(), newFile, pb);
                                        Platform.runLater(() -> {
                                            pb.beenden();
                                        });
                                    }
                                }.start();

                            }

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        AlertBox.display("Error!", "Message too long!");
                    }
                }
            } else {
                AlertBox.display("Error!", "Choose an image!");
            }
        });

        rechts.getChildren().addAll(speichern);
        tab0Layout.setRight(rechts);

        // Tab1
        Tab tab1 = new Tab("Read from Image");
        tab1.setClosable(false);
        BorderPane tab1Layout = new BorderPane();
        tab1.setContent(tab1Layout);

        // Tab1 Links
        Button einlesen = new Button("Choose Image");
        BorderPane.setAlignment(einlesen, Pos.CENTER);
        einlesen.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Which image do you want to read from?");
            fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
            fc.getExtensionFilters().addAll(new ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File NewFile = fc.showOpenDialog(null);
            if (NewFile != null) {
                if (NewFile.isFile()) {
                    try {
                        BufferedImage img = ImageIO.read(NewFile);

                        ProgressBox pb = new ProgressBox();
                        pb.display("Loading...", "Reading message...");
                        new Thread() {
                            public void run() {
                                ausgabe.setText(Processing.readFromImage(img));
                                Platform.runLater(() -> {
                                    pb.beenden();
                                });
                            }
                        }.start();

                    } catch (IOException e1) {
                        AlertBox.display("Error!", "File could not be loaded!");
                    }
                }
            }
        });

        VBox tab1Left = new VBox();
        tab1Left.setPadding(new Insets(5));
        tab1Left.setSpacing(5);
        tab1Left.setAlignment(Pos.CENTER);
        tab1Left.getChildren().add(einlesen);
        tab1Layout.setLeft(tab1Left);

        // Tab1 Middle
        ausgabe = new TextArea();
        ausgabe.setWrapText(true);
        BorderPane.setAlignment(ausgabe, Pos.CENTER);
        ausgabe.setEditable(false);
        ausgabe.setPromptText("Message output here!");

        VBox tab1Middle = new VBox();
        tab1Middle.setPadding(new Insets(5));
        tab1Middle.setSpacing(5);
        tab1Middle.setAlignment(Pos.CENTER);
        tab1Middle.getChildren().add(ausgabe);
        tab1Layout.setCenter(tab1Middle);

        getTabs().addAll(tab0, tab1);

    }

}
