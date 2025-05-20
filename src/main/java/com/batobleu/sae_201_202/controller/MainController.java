package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.exception.InvalidPositionException;
import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.*;
import com.batobleu.sae_201_202.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class MainController extends Application {
    public static MapTile Rock = new TileNotReachable("/Image/Rock.png", "Rocher");
    public static MapTile Cactus = new TileHerb("/Image/Cactus.png", "Cactus", 0.5f);
    public static MapTile Herb = new TileHerb("/Image/Herb.png", "Herbe", 1f);
    public static MapTile Poppy = new TileHerb("/Image/Flower.png", "Marguerite", 2f);
    public static MapTile Exit = new TileExit("/Image/Exit.png", "Sortie");
    public static MapTile Wolf = new TileEntity("/Image/Wolf.png", "Loup");
    public static MapTile Sheep = new TileEntity("/Image/Sheep.png", "Mouton");

    private Simulation s;
    private MenuSelectItems msi;
    private Map map;
    private Stage stage;

    private BorderPane root;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        PopupNewLabyrinth p = new PopupNewLabyrinth();
        p.popupNewLabyrinth();

        this.s = new Simulation(p.getHauteur(), p.getLargeur());

        this.root = new BorderPane();

        new InformationDebug(this);

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Mange moi si tu peux !");
        stage.setScene(scene);
        stage.setResizable(false);

        this.map = new Map(this);
        this.msi = new MenuSelectItems(this);

        MenuBarUp t = new MenuBarUp(root);
        t.addMenuBar();

        this.map.addMap();
        this.msi.switchToMenuDecor();

        stage.show();
    }

    public Simulation getSimulation() {
        return this.s;
    }

    public BorderPane getRoot() {
        return this.root;
    }

    public Map getMap() {
        return this.map;
    }

    public MenuSelectItems getMsi() {
        return this.msi;
    }

    public Stage getStage() { return this.stage; }

    public void updateMapAndSimulation(int x, int y, MapTile selectedItem) throws InvalidPositionException {
        if(!selectedItem.isValidPosition(x, y, this.s.getNx(), this.s.getNy(), this.s.getMap()[y][x])) {
            throw new InvalidPositionException();
        }

        /*
        * Dans le cas ou selectedItem représente une entité:
        *  - entity1 sera l'entité, dans simulation, qui correspond a l'entité dans selectedItem
        *  - entity2 sera l'autre
        * Sinon:
        *  - entity1 sera l'entité, dans la simulation, qui correspond au loup
        *  - entity2 sera le mouton
        */
        Entity entity1 = selectedItem instanceof TileEntity ? (selectedItem == Wolf ? this.s.getWolf() : this.s.getSheep()) : this.s.getWolf();
        Entity entity2 = selectedItem instanceof TileEntity ? (selectedItem == Wolf ? this.s.getSheep() : this.s.getWolf()) : this.s.getSheep();

        // Si on a selectionné une entité
        if(selectedItem instanceof TileEntity) {
            // Si on positionne une entité sur une autre, on supprime la seconde entité
            if(entity2 != null && x == entity2.getX() && y == entity2.getY()) {
                this.s.killEntity(entity2);
            }
            // Si on déplace une entité, on fait apparaitre l'image du décort correspondant
            if(entity1 != null) {
                int _x = entity1.getX(), _y = entity1.getY();
                this.map.updateImage(_x, _y, this.s.getMap()[_y][_x]);
            }

            // On met a jour l'entité dans la simulation
            this.s.setEntity(selectedItem, x, y);
        }
        else {
            // Si le décort ce trouve sur une entité, on supprime l'entité correspondant
            if(entity1 != null && x == entity1.getX() && y == entity1.getY()){
                this.s.killEntity(entity1);
            }
            else if(entity2 != null && x == entity2.getX() && y == entity2.getY()){
                this.s.killEntity(entity2);
            }

            // Cas de la sortie
            if(selectedItem instanceof TileExit) {
                List<Integer> pos = this.s.findExitMapTile();

                if(pos != null) {
                    this.s.getMap()[pos.get(1)][pos.get(0)] = Rock;
                    this.map.updateImage(pos.get(0), pos.get(1), Rock);
                }
            }

            // On met a jour la map avec le décor correspondant
            this.s.getMap()[y][x] = selectedItem;
        }

        // On met a jour l'affichage
        this.map.updateImage(x, y, selectedItem);
    }


}