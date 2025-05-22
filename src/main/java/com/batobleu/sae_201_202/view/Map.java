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

// Composant pour afficher une map
public class Map {
    private MainController mc;

    private Rectangle[][] validPositionIndicators;  // Liste des indicateurs de position valides pour l'élément sélectionné
    private ImageView[][] images;                   // Liste des images

    private VBox container;

    public Map(MainController mc) {
        this.mc = mc;
        this.validPositionIndicators = new Rectangle[this.mc.getSimulation().getNy()][this.mc.getSimulation().getNx()];
        this.images = new ImageView[this.mc.getSimulation().getNy()][this.mc.getSimulation().getNx()];
    }

    public void addMap(){
        // Supprime la map précédent
        this.mc.getRoot().getChildren().remove(this.container);

        // Container de la nouvelle map
        this.container = new VBox();
        this.container.setAlignment(Pos.CENTER);

        // Responsive des cellules de la map en fonction de la taille disponible
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

        // Parcours des éléments de la map dans simulation
        for (int y = 0; y < this.mc.getSimulation().getNy(); y++){
            // Container d'une ligne
            HBox h = new HBox();
            h.setAlignment(Pos.CENTER);
            for (int x = 0; x < this.mc.getSimulation().getNx(); x++) {
                // Container d'une cellule
                StackPane tile = new StackPane();

                // Rectangle de l'indicateur de position valide
                Rectangle validPossitionRectangle = new Rectangle();
                validPossitionRectangle.widthProperty().bind(sizeSquare);
                validPossitionRectangle.heightProperty().bind(sizeSquare);

                // Rectangle de la bordure
                Rectangle borderRectangle = new Rectangle();
                borderRectangle.widthProperty().bind(sizeSquare);
                borderRectangle.heightProperty().bind(sizeSquare);
                borderRectangle.setStroke(Color.BLACK);
                borderRectangle.setStrokeWidth(1);
                borderRectangle.setFill(Color.WHITE);

                // Icone
                ImageView imageV = new ImageView();
                imageV.fitHeightProperty().bind(sizeSquare);
                imageV.fitWidthProperty().bind(sizeSquare);

                // ajout des éléments dans le container de la cellule
                tile.getChildren().addAll(borderRectangle, validPossitionRectangle, imageV);

                // ajout du container de la cellule au container de la ligne
                h.getChildren().addAll(tile);

                this.validPositionIndicators[y][x] = validPossitionRectangle;
                this.images[y][x] = imageV;

                // Choix de l'image à afficher
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
            // Ajout du container de la ligne au container principal
            this.container.getChildren().add(h);
        }

        this.mc.getRoot().setCenter(this.container);

        EventManager.addValidPositionEventOnMap(this.mc);
        EventManager.addEventOnClickMap(this.mc);
    }

    // Getter
    public Rectangle getValidPositionIndicator(int x, int y) {
        return this.validPositionIndicators[y][x];
    }
    public ImageView[][] getImages() {
        return this.images;
    }

    // Permet de mettre à jour l'icone affiché dans une cellule
    public void updateImage(int x, int y, MapTile mt) {
        Image image = new Image(getClass().getResource(mt.getPathIcon()).toExternalForm());
        this.images[y][x].setImage(image);
    }
}
