package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.EventManager;
import com.batobleu.sae_201_202.controller.MainController;
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

import static java.lang.Math.min;

public class Map {
    private MainController mc;

    private Rectangle[][] validPositionIndicators;
    private Rectangle[][] images;

    private VBox container;

    public Map(MainController mc) {
        this.mc = mc;
        this.validPositionIndicators = new Rectangle[this.mc.getSimulation().getNy()][this.mc.getSimulation().getNx()];
        this.images = new Rectangle[this.mc.getSimulation().getNy()][this.mc.getSimulation().getNx()];
    }

    public void addMap(){
        if(this.container != null) {
            this.mc.getRoot().getChildren().remove(this.container);
        }

        this.container = new VBox();
        this.container.setTranslateX(400);
        this.container.setTranslateY(50);

        double width = 500 / (double)this.mc.getSimulation().getNx();
        double height = 500 / (double)this.mc.getSimulation().getNy();

        double sizeSquare = min(width, height);

        for (int y = 0; y < this.mc.getSimulation().getNy(); y++){
            HBox h = new HBox();
            for (int x = 0; x < this.mc.getSimulation().getNx(); x++) {
                Group tile = new Group();

                Rectangle validPossitionRectangle = new Rectangle(sizeSquare, sizeSquare);

                Rectangle imageRectangle = new Rectangle(sizeSquare, sizeSquare);
                imageRectangle.setStroke(Color.BLACK);
                imageRectangle.setStrokeWidth(1);

                tile.getChildren().addAll(validPossitionRectangle, imageRectangle);

                h.getChildren().addAll(tile);

                this.validPositionIndicators[y][x] = validPossitionRectangle;
                this.images[y][x] = imageRectangle;

                this.updateImage(x, y, this.mc.getSimulation().getMap()[y][x]);
            }
            this.container.getChildren().add(h);
        }

        this.mc.getRoot().getChildren().add(this.container);

        EventManager.addValidPositionEventOnMap(this.mc);
        EventManager.addEventOnClickMap(this.mc);
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
