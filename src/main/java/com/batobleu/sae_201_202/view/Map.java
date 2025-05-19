package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.MapTile;
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
    private Rectangle[][] images;

    public Map(Group root, Simulation simulation) {
        this.root = root;
        this.scene = root.getScene();
        this.simulation = simulation;
        validPositionIndicators = new Rectangle[this.simulation.getNy()][this.simulation.getNx()];
        images = new Rectangle[this.simulation.getNy()][this.simulation.getNx()];
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
                Group tile = new Group();

                Rectangle validPossitionRectangle = new Rectangle(width, height);

                Rectangle imageRectangle = new Rectangle(width, height);
                imageRectangle.setStroke(Color.BLACK);
                imageRectangle.setStrokeWidth(1);

                tile.getChildren().addAll(validPossitionRectangle, imageRectangle);

                h.getChildren().addAll(tile);

                this.validPositionIndicators[y][x] = validPossitionRectangle;
                this.images[y][x] = imageRectangle;

                this.updateImage(x, y, this.simulation.getMap()[y][x]);
            }
            v.getChildren().add(h);
        }

        root.getChildren().add(v);
    }

    public Rectangle getValidPositionIndicator(int x, int y) {
        return this.validPositionIndicators[y][x];
    }

    public Rectangle[][] getImages() {
        return this.images;
    }

    public void updateImage(int x, int y, MapTile mt) {
        Image image = new Image(getClass().getResource(mt.getPathIcon()).toExternalForm());
        ImagePattern pattern = new ImagePattern(image);
        this.images[y][x].setFill(pattern);
    }
}
