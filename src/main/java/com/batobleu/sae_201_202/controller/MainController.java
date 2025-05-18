package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.*;
import com.batobleu.sae_201_202.view.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainController extends Application {
    public static MapTile Rock = new TileNotReachable("/Image/Rock.png", "Rocher");
    public static MapTile Cactus = new TileHerb("/Image/Cactus.png", "Cactus", 0.5f);
    public static MapTile Herb = new TileHerb("/Image/Herb.png", "Herbe", 1f);
    public static MapTile Poppy = new TileHerb("/Image/Flower.png", "Marguerite", 2f);
    public static MapTile Exit = new TileExit("/Image/Exit.png", "Sortie");
    public static MapTile Wolf = new TileEntity("/Image/Wolf.png", "Loup");
    public static MapTile Sheep = new TileEntity("/Image/Sheep.png", "Mouton");

    @Override
    public void start(Stage stage) {
        Simulation s = new Simulation(10,10);

        Group root = new Group();

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);

        MenuSelectItems msi = new MenuSelectItems(root);
        Map m = new Map(root, s);
        m.addMap();

        MenuBarUp t = new MenuBarUp(root);
        t.addMenuBar();

        msi.currSelectedProperty().addListener((observable, oldValue, newValue) -> {
            for(int y = 0; y < s.getNy(); y++) {
                for(int x = 0; x < s.getNx(); x++) {
                    if(newValue.isValidPosition(x, y, s.getNx(), s.getNy(), s.getMap()[y][x])) {
                        m.getValidPositionIndicator(x, y).setFill(new Color(0, 0.78, 0.33, 0.5));
                    }
                    else {
                        m.getValidPositionIndicator(x, y).setFill(new Color(0.83, 0.18, 0.18, 0.5));
                    }
                }
            }
        });
        msi.switchToMenuDecor();

        stage.show();
    }
}