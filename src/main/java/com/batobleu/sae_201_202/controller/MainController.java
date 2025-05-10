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

        Scene scene = new Scene(root,1280,720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        Rectangle rectangle = new Rectangle();


        //Rectangle Décor map (Cactus, Marguerite, ...)
        rectangle.setHeight(450);
        rectangle.setWidth(300);
        rectangle.setX(50);
        rectangle.setY(50);
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.BLACK);
        root.getChildren().add(rectangle);

        //Label décor
        Label l1 = new Label("Décor");
        l1.setTranslateX(60);
        l1.setTranslateY(55);
        root.getChildren().add(l1);

        //Tout les Carrés des décors avec leurs label



        //Cactus
        Rectangle r1 = new Rectangle(50,50);
        r1.setX(60);
        r1.setY(80);
        r1.setStroke(Color.BLACK);
        root.getChildren().add(r1);
        Label Cactus = new Label("Cactus");
        Cactus.setTranslateX(120);
        Cactus.setTranslateY(95);
        root.getChildren().add(Cactus);

        //Marguerite
        Rectangle r2 = new Rectangle(r1.getHeight(), r1.getWidth());
        r2.setX(r1.getX());
        r2.setY(r1.getY()+70);
        r2.setStroke(Color.BLACK);
        root.getChildren().add(r2);
        Label Marguerite = new Label("Marguerite");
        Marguerite.setTranslateX(Cactus.getTranslateX());
        Marguerite.setTranslateY(Cactus.getTranslateY()+70);
        root.getChildren().add(Marguerite);

        //Rocher
        Rectangle r3 = new Rectangle(r2.getHeight(), r2.getWidth());
        r3.setX(r2.getX());
        r3.setY(r2.getY()+70);
        r3.setStroke(Color.BLACK);
        root.getChildren().add(r3);
        Label Rocher = new Label("Rocher");
        Rocher.setTranslateX(Marguerite.getTranslateX());
        Rocher.setTranslateY(Marguerite.getTranslateY()+70);
        root.getChildren().add(Rocher);

        //Herbe
        Rectangle r4 = new Rectangle(r3.getHeight(), r3.getWidth());
        r4.setX(r3.getX());
        r4.setY(r3.getY()+70);
        r4.setStroke(Color.BLACK);
        root.getChildren().add(r4);
        Label Herbe = new Label("Herbe");
        Herbe.setTranslateX(Rocher.getTranslateX());
        Herbe.setTranslateY(Rocher.getTranslateY()+70);
        root.getChildren().add(Herbe);

        //sortie
        Rectangle r5 = new Rectangle(r4.getHeight(), r4.getWidth());
        r5.setX(r4.getX());
        r5.setY(r4.getY()+70);
        r5.setStroke(Color.BLACK);
        root.getChildren().add(r5);
        Label Sortie = new Label("Sortie");
        Sortie.setTranslateX(Herbe.getTranslateX());
        Sortie.setTranslateY(Herbe.getTranslateY()+70);
        root.getChildren().add(Sortie);

        //Boutton Reset
        Button Reset = new Button();
        Reset.setText("Reset");
        Reset.setTranslateX(r5.getX());
        Reset.setTranslateY(r5.getY()+70);
        Reset.setPrefHeight(30);
        Reset.setPrefWidth(110);
        root.getChildren().add(Reset);

        //Boutton Valider
        Button Valider = new Button();
        Valider.setText("Valider");
        Valider.setTranslateX(180);
        Valider.setTranslateY(r5.getY()+70);
        Valider.setPrefHeight(30);
        Valider.setPrefWidth(110);
        root.getChildren().add(Valider);

    }

    private Label welcomeText;

    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}