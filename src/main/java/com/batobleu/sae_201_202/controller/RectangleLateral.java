package com.batobleu.sae_201_202.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class RectangleLateral {

    Scene scene;
    Group root;
    Group g;

    public RectangleLateral(Scene s, Group r) {
        scene = s;
        root = r;
        g = new Group();
    }

    public void ajoutElement(){
        Rectangle rectangle = new Rectangle();
        //Rectangle Décor map (Cactus, Marguerite, ...)
        rectangle.setHeight(450);
        rectangle.setWidth(300);
        rectangle.setX(50);
        rectangle.setY(50);
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.BLACK);
        g.getChildren().add(rectangle);

        //Label décor
        Label l1 = new Label("Décor");
        l1.setTranslateX(60);
        l1.setTranslateY(55);
        g.getChildren().add(l1);

        //Tout les Carrés des décors avec leurs label
        //Cactus
        Rectangle r1 = rectangle(0,"Cactus");
        //Marguerite
        Rectangle r2 = rectangle(1,"Marguerite");
        //Rocher
        Rectangle r3 = rectangle(2,"Rocher");
        //Herbe
        Rectangle r4 = rectangle(3,"Herbe");
        //sortie
        Rectangle r5 = rectangle(4,"Exit");
        //Boutton Reset
        Button Reset = new Button();
        Reset.setText("Reset");
        Reset.setTranslateX(r5.getX());
        Reset.setTranslateY(r5.getY()+70);
        Reset.setPrefHeight(30);
        Reset.setPrefWidth(110);
        g.getChildren().add(Reset);

        //Boutton Valider
        Button Valider = new Button();
        Valider.setText("Valider");
        Valider.setTranslateX(180);
        Valider.setTranslateY(r5.getY()+70);
        Valider.setPrefHeight(30);
        Valider.setPrefWidth(110);
        g.getChildren().add(Valider);
        root.getChildren().add(g);

        Valider.setOnAction((ActionEvent e) ->{
            root.getChildren().remove(g);
            MenuEntité m = new MenuEntité(scene, root, this);
            m.ajoutElement();
        });

    }

    private Rectangle rectangle(int order, String labelText) {
        Rectangle r = new Rectangle(50, 50);
        r.setX(60);
        r.setY(80 + order * 70);
        r.setStroke(Color.BLACK);
        g.getChildren().add(r);

        Label l = new Label(labelText);
        l.setTranslateX(120);
        l.setTranslateY(95 + 70 * order);
        g.getChildren().add(l);

        String path = "/Image/" + labelText + ".png";
        System.out.println(path);
        Image image = new Image(getClass().getResource(path).toExternalForm());
        ImagePattern pattern = new ImagePattern(image);
        r.setFill(pattern);
        return r;
    }

    public Group getG() {
        return g;
    }
}
