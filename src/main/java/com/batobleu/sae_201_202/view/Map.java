package com.batobleu.sae_201_202.view;

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
    private static String pathIconWolf = "/Image/Wolf.png";
    private static String pathIconSheep = "/Image/Sheep.png";

    private Group root;
    private Scene scene;
    private Simulation simulation;
    private Rectangle[][] validPositionIndicators;

    public Map(Group root, Simulation simulation) {
        this.root = root;
        this.scene = root.getScene();
        this.simulation = simulation;
        validPositionIndicators = new Rectangle[this.simulation.getNy()][this.simulation.getNx()];
    }

    public void addMap(){
        VBox v = new VBox();
        v.setTranslateX(400);
        v.setTranslateY(50);

        double width = 500 / (double)this.simulation.getNx();
        double height = 500 / (double)this.simulation.getNy();

        for (int y = 0; y < this.simulation.getNy(); y++){
            HBox h = new HBox();
            for (int x = 0; x < this.simulation.getNx(); x++) {
                String imagePath;

                if(this.simulation.getSheep() != null && this.simulation.getSheep().getY() == y && this.simulation.getSheep().getX() == x) {
                    imagePath = pathIconSheep;
                }
                else if(this.simulation.getWolf() != null && this.simulation.getWolf().getY() == y && this.simulation.getWolf().getX() == x) {
                    imagePath = pathIconWolf;
                }
                else {
                    imagePath = this.simulation.getMap()[y][x].getPathIcon();
                }

                Group tile = new Group();

                Rectangle validPossitionRectangle = new Rectangle(width, height);

                Rectangle imageRectangle = new Rectangle(width, height);
                Image image = new Image(getClass().getResource(imagePath).toExternalForm());
                ImagePattern pattern = new ImagePattern(image);
                imageRectangle.setFill(pattern);
                imageRectangle.setStroke(Color.BLACK);
                imageRectangle.setStrokeWidth(1);

                tile.getChildren().addAll(validPossitionRectangle, imageRectangle);

                h.getChildren().addAll(tile);

                this.validPositionIndicators[y][x] = validPossitionRectangle;
            }
            v.getChildren().add(h);
        }


        root.getChildren().add(v);
    }

    public Rectangle getValidPositionIndicator(int x, int y) {
        return this.validPositionIndicators[y][x];
    }
}
