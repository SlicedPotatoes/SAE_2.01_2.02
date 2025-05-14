package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.model.Simulation;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Simulation s = new Simulation(10,10);

        Group root = new Group();

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        MenuItems r = new MenuItems(root);
        r.addElement();

        Map m = new Map(root, s);
        m.addMap();

        stage.show();
    }
}