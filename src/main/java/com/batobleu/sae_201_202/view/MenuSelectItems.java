package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.tile.MapTile;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MenuSelectItems {
    private Group root;
    private Group group;

    private Button button1;
    private Button button2;

    public MenuSelectItems(Group r) {
        this.root = r;
        this.group = new Group();

        this.button1 = createButton("Reset", 60, 510);
        this.button2 = createButton("Valider", 180, 510);
    }

    public void changeMenu(String titleString, MapTile... mapTiles){
        this.root.getChildren().remove(this.group);
        this.group = new Group();

        Rectangle container = new Rectangle();
        container.setHeight(500);
        container.setWidth(300);
        container.setX(50);
        container.setY(50);
        container.setFill(Color.LIGHTBLUE);
        container.setStroke(Color.BLACK);

        Label titleLabel = new Label(titleString);
        titleLabel.setTranslateX(60);
        titleLabel.setTranslateY(55);

        this.group.getChildren().addAll(container, titleLabel, this.button1, this.button2);

        for(int i = 0; i < mapTiles.length; i++) {
            MapTile element = mapTiles[i];
            rectangle(i, element.getLabel(), element.getPathIcon());
        }

        this.root.getChildren().add(this.group);
    }

    private Button createButton(String text, double x, double y) {
        Button b = new Button();
        b.setText(text);
        b.setTranslateX(x);
        b.setTranslateY(y);
        b.setPrefHeight(30);
        b.setPrefWidth(110);

        return b;
    }

    public void switchToMenuDecor() {
        this.changeMenu("Décor", MainController.Cactus, MainController.Poppy, MainController.Rock, MainController.Herb, MainController.Exit);

        this.button1.setText("Reset");
        this.button1.setOnAction((ActionEvent e) -> {

        });

        this.button2.setOnAction((ActionEvent e) -> {
            this.switchToMenuEntity();
        });
    }
    public void switchToMenuEntity() {
        this.changeMenu("Entité", MainController.Wolf, MainController.Sheep);

        this.button1.setText("Retour");
        this.button1.setOnAction((ActionEvent e) -> {
            this.switchToMenuDecor();
        });

        this.button2.setOnAction((ActionEvent e) -> {

        });
    }

    public void rectangle(int order, String labelText, String imagePath) {
        Rectangle r = new Rectangle(50, 50);
        r.setX(60);
        r.setY(80 + order * 70);
        r.setStroke(Color.BLACK);

        Label l = new Label(labelText);
        l.setTranslateX(120);
        l.setTranslateY(95 + 70 * order);

        Image image = new Image(getClass().getResource(imagePath).toExternalForm());
        ImagePattern pattern = new ImagePattern(image);
        r.setFill(pattern);

        this.group.getChildren().addAll(r, l);
    }
}
