package com.batobleu.sae_201_202.controller;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MenuEntité {
    Scene scene;
    Group root;

    public MenuEntité(Scene s, Group r) {
        scene = s;
        root = r;
    }

    public void ajoutElement(){
        Rectangle rectangle = new Rectangle();
        //Rectangle Décor map (Cactus, Marguerite, ...)
        rectangle.setHeight(225);
        rectangle.setWidth(300);
        rectangle.setX(50);
        rectangle.setY(50);
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.BLACK);
        root.getChildren().add(rectangle);

        //Label décor
        Label l1 = new Label("Entité");
        l1.setTranslateX(60);
        l1.setTranslateY(55);
        root.getChildren().add(l1);

        //Tout les Carrés des décors avec leurs label
        //Loup
        Rectangle e1 = rectangle(0,"Loup");
        //Mouton
        Rectangle r2 = rectangle(1,"Mouton");
        //Boutton Reset
        Button Reset = new Button();
        Reset.setText("Retour");
        Reset.setTranslateX(r2.getX());
        Reset.setTranslateY(r2.getY()+70);
        Reset.setPrefHeight(30);
        Reset.setPrefWidth(110);
        root.getChildren().add(Reset);

        //Boutton Valider
        Button Valider = new Button();
        Valider.setText("Valider");
        Valider.setTranslateX(200);
        Valider.setTranslateY(r2.getY()+70);
        Valider.setPrefHeight(30);
        Valider.setPrefWidth(110);
        root.getChildren().add(Valider);

    }
    private Rectangle rectangle(int order, String labelText) {
        Rectangle r = new Rectangle(50, 50);
        r.setX(60);
        r.setY(80 + order * 70);
        r.setStroke(Color.BLACK);
        root.getChildren().add(r);

        Label l = new Label(labelText);
        l.setTranslateX(120);
        l.setTranslateY(95 + 70 * order);
        root.getChildren().add(l);

        String path = "/Image/" + labelText + ".png";
        System.out.println(path);
        Image image = new Image(getClass().getResource(path).toExternalForm());
        ImagePattern pattern = new ImagePattern(image);
        r.setFill(pattern);
        return r;
    }
}
