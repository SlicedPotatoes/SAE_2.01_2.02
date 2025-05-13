package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.model.Case;
import com.batobleu.sae_201_202.model.Simulation;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Simulation s = new Simulation(10,10);

        Group root = new Group();

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        RectangleLateral r = new RectangleLateral(scene, root);
        r.ajoutElement();

        VBox v = new VBox();
        v.setTranslateX(400);
        v.setTranslateY(50);
        Case[][] c = s.getMap();
        for (int i = 0; i < s.getNx(); i++){
            HBox h = new HBox();
            for (int j = 0; j < s.getNy(); j++) {
                Rectangle re = new Rectangle(50, 50);
                if (c[i][j] == Case.Cactus) {
                    String path = "/Image/Cactus.png";
                    Image image = new Image(getClass().getResource(path).toExternalForm());
                    ImagePattern pattern = new ImagePattern(image);
                    re.setFill(pattern);
                    h.getChildren().add(re);
                } else if (c[i][j] == Case.Rock) {
                    String path = "/Image/Rocher.png";
                    Image image = new Image(getClass().getResource(path).toExternalForm());
                    ImagePattern pattern = new ImagePattern(image);
                    re.setFill(pattern);
                    h.getChildren().add(re);
                } else if (c[i][j] == Case.Poppy) {
                    String path = "/Image/Marquerite.png";
                    Image image = new Image(getClass().getResource(path).toExternalForm());
                    ImagePattern pattern = new ImagePattern(image);
                    re.setFill(pattern);
                    h.getChildren().add(re);
                } else if (c[i][j] == Case.Grass) {
                    String path = "/Image/Herbe.png";
                    Image image = new Image(getClass().getResource(path).toExternalForm());
                    ImagePattern pattern = new ImagePattern(image);
                    re.setFill(pattern);
                    h.getChildren().add(re);
                }
                re.setStroke(Color.BLACK);
                re.setStrokeWidth(1);
            }
            v.getChildren().add(h);
        }
        root.getChildren().add(v);
        stage.show();
    }
}