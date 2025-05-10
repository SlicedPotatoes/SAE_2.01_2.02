package com.batobleu.sae_201_202.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        RectangleLateral r = new RectangleLateral(scene, root);
        r.ajoutElement();

        stage.show();
    }
}