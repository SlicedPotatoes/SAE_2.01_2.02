package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.EventManager;
import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.sun.tools.javac.Main;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class MenuSelectItems {
    private MainController mc;
    private Group group;

    private Button button1;
    private Button button2;

    private ObjectProperty<MapTile> currSelected;
    private Rectangle currSelectedRectangle;

    public MenuSelectItems(MainController mc) {
        this.mc = mc;

        this.button1 = createButton("Reset", 60, 510);
        this.button2 = createButton("Valider", 180, 510);

        this.currSelected = new SimpleObjectProperty<>();
    }

    public void switchToMenuDecor() {
        this.changeMenu("Décor", Cactus, Cactus, Poppy, Rock, Herb, Exit);

        this.button1.setText("Reset");

        EventManager.addEventResetButton(this.button1, this.mc, this.currSelected);
        EventManager.addEventSwitchMenuEntity(this.button2, this);
    }

    public void switchToMenuEntity() {
        this.changeMenu("Entité", Wolf, Wolf, Sheep);

        this.button1.setText("Retour");

        EventManager.addEventSwitchMenuDecor(this.button1, this);
        EventManager.addEventSwitchToSimulation(this.button2);
    }

    public ObjectProperty<MapTile> currSelectedProperty() {
        return this.currSelected;
    }

    public void setSelected(Rectangle r, MapTile mt) {
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

        this.group.getChildren().addAll(r, l);
        EventManager.addEventClickSelection(r, mt, this);

        return r;
    }

    private void changeMenu(String titleString, MapTile defaultSelected, MapTile... mapTiles){
        this.mc.getRoot().getChildren().remove(this.group);
        this.group = new Group();

        Rectangle container = new Rectangle(50,50,300,500);
        container.setFill(Color.LIGHTBLUE);
        container.setStroke(Color.BLACK);

        Label titleLabel = new Label(titleString);
        titleLabel.setTranslateX(60);
        titleLabel.setTranslateY(55);

        //Rectangle des aides
        Rectangle help = new Rectangle(315,55,25,25);
        help.setMouseTransparent(false);
        help.setFill(new ImagePattern(new Image(getClass().getResource("/Image/Help.png").toExternalForm())));

        Tooltip t = new Tooltip("Selectionner un objet et cliquer sur la grille pour le positionner");
        Tooltip.install(help, t);

        EventManager.addEventTooltipShow(help, t);
        EventManager.addEventTooltipHide(help, t);

        this.group.getChildren().addAll(container, help, titleLabel, this.button1, this.button2);

        for(int i = 0; i < mapTiles.length; i++) {
            Rectangle r = this.createRectangle(i, mapTiles[i]);

            if(mapTiles[i].equals(defaultSelected)) {
                this.setSelected(r, defaultSelected);
            }
        }

        this.mc.getRoot().getChildren().add(this.group);
    }
}
