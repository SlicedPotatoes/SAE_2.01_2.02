package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.model.tile.TileExit;
import com.batobleu.sae_201_202.model.tile.TileHerb;
import com.batobleu.sae_201_202.model.tile.TileNotReachable;
import com.batobleu.sae_201_202.view.Map;
import com.batobleu.sae_201_202.view.MenuSelectItems;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController extends Application {
    public static MapTile Rock = new TileNotReachable("/Image/Rock.png", "Rocher");
    public static MapTile Cactus = new TileHerb("/Image/Cactus.png", "Cactus", 0.5f);
    public static MapTile Herb = new TileHerb("/Image/Herb.png", "Herbe", 1f);
    public static MapTile Poppy = new TileHerb("/Image/Flower.png", "Marguerite", 2f);
    public static MapTile Exit = new TileExit("/Image/Exit.png", "Sortie");
    public static MapTile Wolf = new MapTile("/Image/Wolf.png", "Loup");
    public static MapTile Sheep = new MapTile("/Image/Sheep.png", "Mouton");

    @Override
    public void start(Stage stage) {
        Simulation s = new Simulation(10,10);

        Group root = new Group();

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        MenuSelectItems msi = new MenuSelectItems(root);
        msi.switchToMenuDecor();

        Map m = new Map(root, s);
        m.addMap();

        stage.show();
    }
}