package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.MapTile;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.batobleu.sae_201_202.controller.MainController.*;
import static java.lang.Math.min;

public class Map {
    private MainController mc;

    private Rectangle[][] validPositionIndicators;
    private ImageView[][] images;

    private VBox container;

    public Map(MainController mc) {
        this.mc = mc;
        this.validPositionIndicators = new Rectangle[this.mc.getSimulation().getNy()][this.mc.getSimulation().getNx()];
        this.images = new ImageView[this.mc.getSimulation().getNy()][this.mc.getSimulation().getNx()];
    }

    public void addMap(){
        if(this.container != null) {
            this.mc.getRoot().getChildren().remove(this.container);
        }

        this.container = new VBox();
        this.container.setAlignment(Pos.CENTER);

        DoubleBinding width = this.container.widthProperty().multiply(0.9).divide((double)this.mc.getSimulation().getNx());
        DoubleBinding height = this.container.heightProperty().multiply(0.9).divide((double)this.mc.getSimulation().getNy());

        DoubleBinding sizeSquare = new DoubleBinding() {
            {
                super.bind(width, height);
            }

            @Override
            protected double computeValue() {
                return min(width.get(), height.get());
            }
        };

        Entity wolf = this.mc.getSimulation().getWolf();
        Entity sheep = this.mc.getSimulation().getSheep();

        for (int y = 0; y < this.mc.getSimulation().getNy(); y++){
            HBox h = new HBox();
            h.setAlignment(Pos.CENTER);
            for (int x = 0; x < this.mc.getSimulation().getNx(); x++) {
                StackPane tile = new StackPane();

                Rectangle validPossitionRectangle = new Rectangle();
                validPossitionRectangle.widthProperty().bind(sizeSquare);
                validPossitionRectangle.heightProperty().bind(sizeSquare);

                Rectangle borderRectangle = new Rectangle();
                borderRectangle.widthProperty().bind(sizeSquare);
                borderRectangle.heightProperty().bind(sizeSquare);
                borderRectangle.setStroke(Color.BLACK);
                borderRectangle.setStrokeWidth(1);
                borderRectangle.setFill(Color.WHITE);

                ImageView imageV = new ImageView();
                imageV.fitHeightProperty().bind(sizeSquare);
                imageV.fitWidthProperty().bind(sizeSquare);

                tile.getChildren().addAll(borderRectangle, validPossitionRectangle, imageV);

                h.getChildren().addAll(tile);

                this.validPositionIndicators[y][x] = validPossitionRectangle;

                this.images[y][x] = imageV;

                if(wolf != null && wolf.getX() == x && wolf.getY() == y) {
                    this.updateImage(x, y, WOLF);
                }
                else if(sheep != null && sheep.getX() == x && sheep.getY() == y) {
                    this.updateImage(x, y, SHEEP);
                }
                else {
                    this.updateImage(x, y, this.mc.getSimulation().getMap()[y][x]);
                }
            }
            this.container.getChildren().add(h);
        }

        this.mc.getRoot().setCenter(this.container);

        EventManager.addValidPositionEventOnMap(this.mc);
        EventManager.addEventOnClickMap(this.mc);
    }

    public Rectangle getValidPositionIndicator(int x, int y) {
        return this.validPositionIndicators[y][x];
    }

    public ImageView[][] getImages() {
        return this.images;
    }

    public void updateImage(int x, int y, MapTile mt) {
        Image image = new Image(getClass().getResource(mt.getPathIcon()).toExternalForm());
        this.images[y][x].setImage(image);
    }
}
