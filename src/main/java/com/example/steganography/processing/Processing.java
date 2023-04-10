package com.example.steganography.processing;

import com.example.steganography.gui.AlertBox;
import com.example.steganography.gui.ProgressBox;
import javafx.application.Platform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Processing {

    static final int min = 99;
    static final int max = 250;

    public static void saveInImage(BufferedImage img, String text, File f, ProgressBox pb) {

        System.out.println("saving in image" + img.getWidth() + " " + img.getHeight());
        text = text.length() + "." + text;

        String neuerText = "";

        for (char c : text.toCharArray()) {

            if (((int) c + "").length() == 1) {
                neuerText = neuerText + 0 + 0 + (int) c;
            } else if (((int) c + "").length() == 2) {
                neuerText = neuerText + 0 + (int) c;
            } else if (((int) c + "").length() == 3) {
                neuerText = neuerText + (int) c;
            } else {
                System.out.println("Unknown character");
                neuerText = neuerText + "063";
            }
        }

        int anz = 0;
        speicherung:
        {

            for (int h = 0; h < img.getHeight(); h++) {
                for (int w = 0; w < img.getWidth(); w++) {

                    if (anz == neuerText.length()) {
                        System.out.println("Finished");
                        break speicherung;
                    }

                    Color col = new Color(img.getRGB(w, h));
                    int farbe = strongestColor(col.getRGB());

                    if (farbe == 1) {
                        if (col.getRed() > min && col.getRed() < max) {
                            int red = Integer
                                    .parseInt((col.getRed() + "").substring(0, (col.getRed() + "").length() - 1)
                                            + neuerText.charAt(anz));
                            // only save if the color is still the strongest after saving
                            if (red - (red % 10) != col.getGreen() - (col.getGreen() % 10)
                                    && red - (red % 10) != col.getBlue() - (col.getBlue() % 10)) {
                                img.setRGB(w, h, new Color(red, col.getGreen(), col.getBlue()).getRGB());
                                anz++;
                                System.out.println(anz + " / " + neuerText.length());

                                final int fixAnz = anz;
                                final int fixLength = neuerText.length();
                            }
                        }
                    } else if (farbe == 2) {
                        if (col.getGreen() > min && col.getGreen() < max) {
                            int green = Integer
                                    .parseInt((col.getGreen() + "").substring(0, (col.getGreen() + "").length() - 1)
                                            + neuerText.charAt(anz));
                            // only save if the color is still the strongest after saving
                            if (green - (green % 10) != col.getRed() - (col.getRed() % 10)
                                    && green - (green % 10) != col.getBlue() - (col.getBlue() % 10)) {
                                img.setRGB(w, h, new Color(col.getRed(), green, col.getBlue()).getRGB());
                                anz++;
                                System.out.println(anz + " / " + neuerText.length());

                                final int fixAnz = anz;
                                final int fixLength = neuerText.length();
                            }
                        }
                    } else if (farbe == 3) {
                        if (col.getBlue() > min && col.getBlue() < max) {
                            int blue = Integer
                                    .parseInt((col.getBlue() + "").substring(0, (col.getBlue() + "").length() - 1)
                                            + neuerText.charAt(anz));
                            // only save if the color is still the strongest after saving
                            if (blue - (blue % 10) != col.getGreen() - (col.getGreen() % 10)
                                    && blue - (blue % 10) != col.getRed() - (col.getRed() % 10)) {
                                img.setRGB(w, h, new Color(col.getRed(), col.getGreen(), blue).getRGB());
                                anz++;
                                System.out.println(anz + " / " + neuerText.length());

                                final int fixAnz = anz;
                                final int fixLength = neuerText.length();
                            }
                        }
                    }
                }
            }
        }

        try {
            System.out.println(neuerText);
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            Platform.runLater(() -> {
                AlertBox.display("Error!", "Could not save!");
            });
        }

    }

    private static int strongestColor(int farbe) {
        Color col = new Color(farbe);

        if (col.getRed() >= col.getBlue() && col.getRed() >= col.getGreen()) {
            return 1;
        } else if (col.getGreen() >= col.getBlue() && col.getGreen() >= col.getRed()) {
            return 2;
        } else if (col.getBlue() >= col.getRed() && col.getBlue() >= col.getGreen()) {
            return 3;
        } else {
            System.out.println("Error finding Color");
            return 0;
        }
    }

    public static int numberUsablePixels(BufferedImage img) {

        int anz = 0;

        for (int h = 0; h < img.getHeight(); h++) {
            for (int w = 0; w < img.getWidth(); w++) {

                Color col = new Color(img.getRGB(w, h));
                int farbe = strongestColor(col.getRGB());

                switch (farbe) {
                    default:
                        Platform.runLater(() -> {
                            AlertBox.display("Error!", "Could not load message!");
                        });
                        break;
                    case 1:
                        if (col.getRed() > min && col.getRed() < max) {
                            if (col.getRed() - (col.getRed() % 10) != col.getGreen() - (col.getGreen() % 10)
                                    && col.getRed() - (col.getRed() % 10) != col.getBlue() - (col.getBlue() % 10)) {
                                anz++;
                            }
                        }
                        break;
                    case 2:
                        if (col.getGreen() > min && col.getGreen() < max) {
                            if (col.getGreen() - (col.getGreen() % 10) != col.getRed() - (col.getRed() % 10)
                                    && col.getGreen() - (col.getGreen() % 10) != col.getBlue() - (col.getBlue() % 10)) {
                                anz++;
                            }
                        }
                        break;
                    case 3:
                        if (col.getBlue() > min && col.getBlue() < max) {
                            if (col.getBlue() - (col.getBlue() % 10) != col.getRed() - (col.getRed() % 10)
                                    && col.getBlue() - (col.getBlue() % 10) != col.getGreen() - (col.getGreen() % 10)) {
                                anz++;
                            }
                        }
                        break;
                }
            }
        }
        return anz;
    }

    public static String readFromImage(BufferedImage img) {

        String neuerInhalt = "";

        try {

            String inhalt = "";
            boolean LengthAlreadyFound = false;
            int laenge = 0;
            int anz = 0;

            BuchstabenSuche:
            {

                for (int y = 0; y < img.getHeight(); y++) {
                    for (int x = 0; x < img.getWidth(); x++) {

                        Color col = new Color(img.getRGB(x, y));
                        int farbe = strongestColor(col.getRGB());

                        if (anz == laenge && LengthAlreadyFound) {
                            break BuchstabenSuche;
                        }

                        switch (farbe) {
                            default:
                                Platform.runLater(() -> {
                                    AlertBox.display("Error!", "Could not load message!");
                                });
                                break;
                            case 1:
                                if (col.getRed() > min && col.getRed() < max) {

                                    if (col.getRed() - (col.getRed() % 10) != col.getGreen() - (col.getGreen() % 10)
                                            && col.getRed() - (col.getRed() % 10) != col.getBlue() - (col.getBlue() % 10)) {

                                        inhalt = inhalt + (col.getRed() + "").substring((col.getRed() + "").length() - 1);
                                        if (LengthAlreadyFound) {
                                            System.out.println(anz + "/" + laenge);
                                            anz++;
                                        }
                                    }
                                }
                                break;
                            case 2:
                                if (col.getGreen() > min && col.getGreen() < max) {

                                    if (col.getGreen() - (col.getGreen() % 10) != col.getRed() - (col.getRed() % 10)
                                            && col.getGreen() - (col.getGreen() % 10) != col.getBlue()
                                            - (col.getBlue() % 10)) {

                                        inhalt = inhalt
                                                + (col.getGreen() + "").substring((col.getGreen() + "").length() - 1);
                                        if (LengthAlreadyFound) {
                                            System.out.println(anz + "/" + laenge);
                                            anz++;
                                        }
                                    }
                                }
                                break;
                            case 3:
                                if (col.getBlue() > min && col.getBlue() < max) {

                                    if (col.getBlue() - (col.getBlue() % 10) != col.getRed() - (col.getRed() % 10)
                                            && col.getBlue() - (col.getBlue() % 10) != col.getGreen()
                                            - (col.getGreen() % 10)) {

                                        inhalt = inhalt + (col.getBlue() + "").substring((col.getBlue() + "").length() - 1);
                                        if (LengthAlreadyFound) {
                                            System.out.println(anz + "/" + laenge);
                                            anz++;
                                        }
                                    }
                                }
                                break;
                        }

                        if (inhalt.endsWith("046") && !LengthAlreadyFound) {
                            String zwischenSpeicher = "";
                            LengthAlreadyFound = true;

                            while (!inhalt.equals("")) {
                                zwischenSpeicher = zwischenSpeicher + (char) Integer.parseInt(inhalt.substring(0, 3));
                                inhalt = inhalt.substring(3);
                            }
                            zwischenSpeicher = zwischenSpeicher.substring(0, zwischenSpeicher.length() - 1);
                            System.out.println("length is " + zwischenSpeicher);

                            laenge = Integer.parseInt(zwischenSpeicher) * 3;
                        }
                    }
                }

            }

            System.out.println(inhalt);

            while (!inhalt.equals("")) {

                try {
                    neuerInhalt = neuerInhalt + (char) Integer.parseInt(inhalt.substring(0, 3));
                } catch (Exception e) {
                }

                inhalt = inhalt.substring(3);
            }
            System.out.println(neuerInhalt);
        } catch (Exception e) {
            Platform.runLater(() -> {
                AlertBox.display("Error!", "No message found!");
            });
            return "";
        }

        return neuerInhalt;

    }

}
