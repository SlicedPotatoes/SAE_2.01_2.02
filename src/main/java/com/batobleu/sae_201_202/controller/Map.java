package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.model.Case;
import com.batobleu.sae_201_202.model.Simulation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Map {
    Group root;
    Scene scene;
    Simulation map;

    public Map(Group root, Simulation map) {
        this.root = root;
        this.scene = root.getScene();
        this.map = map;
    }

    public void addMap(){
        VBox v = new VBox();
        v.setTranslateX(400);
        v.setTranslateY(50);
        for (int i = 0; i < map.getNx(); i++){
            HBox h = new HBox();
            for (int j = 0; j < map.getNy(); j++) {
                Rectangle re = new Rectangle(50, 50);
                if (map.getMap()[i][j] == Case.Cactus) {
                    String path = "/Image/Cactus.png";
                    Image image = new Image(getClass().getResource(path).toExternalForm());
                    ImagePattern pattern = new ImagePattern(image);
                    re.setFill(pattern);
                    h.getChildren().add(re);
                } else if (map.getMap()[i][j] == Case.Rock) {
                    String path = "/Image/Rocher.png";
                    Image image = new Image(getClass().getResource(path).toExternalForm());
                    ImagePattern pattern = new ImagePattern(image);
                    re.setFill(pattern);
                    h.getChildren().add(re);
                } else if (map.getMap()[i][j] == Case.Poppy) {
                    String path = "/Image/Marguerite.png";
                    Image image = new Image(getClass().getResource(path).toExternalForm());
                    ImagePattern pattern = new ImagePattern(image);
                    re.setFill(pattern);
                    h.getChildren().add(re);
                } else if (map.getMap()[i][j] == Case.Grass) {
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
    }
}
