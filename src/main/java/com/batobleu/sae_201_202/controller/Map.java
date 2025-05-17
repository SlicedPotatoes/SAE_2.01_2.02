package com.batobleu.sae_201_202.controller;

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
    private static String pathIconWolf = "/Image/loup.png";
    private static String pathIconSheep = "/Image/mouton.png";

    Group root;
    Scene scene;
    Simulation simulation;

    public Map(Group root, Simulation simulation) {
        this.root = root;
        this.scene = root.getScene();
        this.simulation = simulation;
    }

    public void addMap(){
        VBox v = new VBox();
        v.setTranslateX(400);
        v.setTranslateY(50);

        for (int i = 0; i < this.simulation.getNy(); i++){
            HBox h = new HBox();
            for (int j = 0; j < this.simulation.getNy(); j++) {
                String imagePath;

                if(this.simulation.getSheep() != null && this.simulation.getSheep().getY() == i && this.simulation.getSheep().getX() == j) {
                    imagePath = pathIconSheep;
                }
                else if(this.simulation.getWolf() != null && this.simulation.getWolf().getY() == i && this.simulation.getWolf().getX() == j) {
                    imagePath = pathIconWolf;
                }
                else {
                    imagePath = this.simulation.getMap()[i][j].getPathIcon();
                }

                Rectangle re = new Rectangle(50, 50);
                Image image = new Image(getClass().getResource(imagePath).toExternalForm());
                ImagePattern pattern = new ImagePattern(image);
                re.setFill(pattern);
                h.getChildren().add(re);
                re.setStroke(Color.BLACK);
                re.setStrokeWidth(1);

            }
            v.getChildren().add(h);
        }


        root.getChildren().add(v);
    }
}
