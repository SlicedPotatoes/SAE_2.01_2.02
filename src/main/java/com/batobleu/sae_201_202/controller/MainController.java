package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.exception.InvalidPositionException;
import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.*;
import com.batobleu.sae_201_202.view.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
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

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        this.s = new Simulation(10,10);

        Group root = new Group();

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);


        this.map = new Map(root, s);
        this.map.addMap();
        this.msi = new MenuSelectItems(root, stage);

        MenuBarUp t = new MenuBarUp(root);
        t.addMenuBar();

        // Ajout des événements pour détecter un changement d'éléments sélectionnés
        this.msi.currSelectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Old: " + (oldValue == null ? "null" : oldValue.getLabel()));
            System.out.println("New: " + newValue.getLabel());

            for(int y = 0; y < s.getNy(); y++) {
                for(int x = 0; x < s.getNx(); x++) {
                    if(newValue.isValidPosition(x, y, this.s.getNx(), this.s.getNy(), this.s.getMap()[y][x])) {
                        this.map.getValidPositionIndicator(x, y).setFill(new Color(0, 0.78, 0.33, 0.5));
                    }
                    else {
                        this.map.getValidPositionIndicator(x, y).setFill(new Color(0.83, 0.18, 0.18, 0.5));
                    }
                }
            }
        });
        this.msi.switchToMenuDecor();

        for (int y = 0; y < s.getNy(); y++) {
            for(int x = 0; x < s.getNx(); x++) {
                this.addEventOnClickMap(x, y);
            }
        }

        stage.show();
    }

    private List<Integer> findExitMapTile() {
        for(int y = 0; y < this.s.getNy(); y++) {
            for(int x = 0; x < this.s.getNx(); x++) {
                if(this.s.getMap()[y][x] == Exit) {
                    List<Integer> pos = new ArrayList<>();
                    pos.add(x);
                    pos.add(y);

                    return pos;
                }
            }
        }

        return null;
    }

    private void updateMapAndSimulation(int x, int y, MapTile selectedItem) throws InvalidPositionException {
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
            // On met a jour la map avec le décort correspondant
            this.s.getMap()[y][x] = selectedItem;

            // Si le décort ce trouve sur une entité, on supprime l'entité correspondant
            if(entity1 != null && x == entity1.getX() && y == entity1.getY()){
                this.s.killEntity(entity1);
            }
            else if(entity2 != null && x == entity2.getX() && y == entity2.getY()){
                this.s.killEntity(entity2);
            }

            // Cas de la sortie
            if(selectedItem instanceof TileExit) {
                List<Integer> pos = findExitMapTile();

                if(pos != null) {
                    this.s.getMap()[pos.get(1)][pos.get(0)] = Rock;
                }
            }
        }

        // On met a jour l'affichage
        this.map.updateImage(x, y, selectedItem);
    }

    private void addEventOnClickMap(int x, int y) {
        this.map.getImages()[y][x].setOnMouseClicked((MouseEvent e) -> {
            MapTile selectedItem = this.msi.currSelectedProperty().get();

            if(selectedItem instanceof TileEntity) {
                Entity entity1 = selectedItem == Wolf ? this.s.getWolf() : this.s.getSheep();
                Entity entity2 = selectedItem == Wolf ? this.s.getSheep() : this.s.getWolf();

                if(entity1 != null) {
                    // Popup pour demander le remplacement
                    System.out.println("Demander le remplacement");
                    return;
                }
                if(entity2 != null && entity2.getX() == x && entity2.getY() == y) {
                    // Popup pour demander le remplacement
                    System.out.println("Demander le remplacement 2");
                    return;
                }
            }
            else if(selectedItem instanceof TileExit) {
                List<Integer> pos = findExitMapTile();

                if(pos != null) {
                    // Popup pour demander le remplacement
                    System.out.println("Demander le remplacement 3");
                    return;
                }
            }

            try {
                this.updateMapAndSimulation(x, y, selectedItem);
            }
            catch (InvalidPositionException ex) {
                System.out.println(ex);
            }

        });
    }
}