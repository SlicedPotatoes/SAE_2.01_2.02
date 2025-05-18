package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.tile.MapTile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MenuSelectItems {
    private Group root;
    private Group group;

    private Button button1;
    private Button button2;

    private ObjectProperty<MapTile> currSelected;
    private Rectangle currSelectedRectangle;

    public MenuSelectItems(Group r) {
        this.root = r;
        this.group = new Group();

        this.button1 = createButton("Reset", 60, 510);
        this.button2 = createButton("Valider", 180, 510);

        this.currSelected = new SimpleObjectProperty<>();
    }

    public void switchToMenuDecor() {
        this.changeMenu("Décor", MainController.Cactus, MainController.Cactus, MainController.Poppy, MainController.Rock, MainController.Herb, MainController.Exit);

        this.button1.setText("Reset");
        this.button1.setOnAction((ActionEvent e) -> {

        });

        this.button2.setOnAction((ActionEvent e) -> {
            this.switchToMenuEntity();
        });
    }

    public void switchToMenuEntity() {
        this.changeMenu("Entité", MainController.Wolf, MainController.Wolf, MainController.Sheep);

        this.button1.setText("Retour");
        this.button1.setOnAction((ActionEvent e) -> {
            this.switchToMenuDecor();
        });

        this.button2.setOnAction((ActionEvent e) -> {

        });
    }

    public ObjectProperty<MapTile> currSelectedProperty() {
        return this.currSelected;
    }

    private void setSelected(Rectangle r, MapTile mt) {
        if(currSelectedRectangle != null) {
            currSelectedRectangle.setStroke(Color.BLACK);
        }

        this.currSelected.set(mt);
        this.currSelectedRectangle = r;
        r.setStroke(Color.BLUE);
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

    private Rectangle createRectangle(int order, MapTile mt) {
        Rectangle r = new Rectangle(50, 50);
        r.setX(60);
        r.setY(80 + order * 70);
        r.setStroke(Color.BLACK);

        Label l = new Label(mt.getLabel());
        l.setTranslateX(120);
        l.setTranslateY(95 + 70 * order);

        Image image = new Image(getClass().getResource(mt.getPathIcon()).toExternalForm());
        ImagePattern pattern = new ImagePattern(image);
        r.setFill(pattern);

        r.setOnMouseClicked((MouseEvent e) -> {
            this.setSelected(r, mt);
        });

        this.group.getChildren().addAll(r, l);

        return r;
    }

    private void changeMenu(String titleString, MapTile defaultSelected, MapTile... mapTiles){
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
            Rectangle r = this.createRectangle(i, mapTiles[i]);

            if(mapTiles[i].equals(defaultSelected)) {
                this.setSelected(r, defaultSelected);
            }
        }

        this.root.getChildren().add(this.group);
    }
}
