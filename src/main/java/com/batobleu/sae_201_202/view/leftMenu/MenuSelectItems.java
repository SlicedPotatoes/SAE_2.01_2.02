package com.batobleu.sae_201_202.view.leftMenu;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.view.EventManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class MenuSelectItems {
    private MainController mc;
    private VBox container;

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

        this.button1.setText("Réinitialiser");

        EventManager.addEventResetButton(this.button1, this.mc, this.currSelected);
        EventManager.addEventSwitchMenuEntity(this.button2, this.mc);
    }

    public void switchToMenuEntity() {
        this.changeMenu("Entité", Wolf, Wolf, Sheep);

        this.button1.setText("Retour");

        EventManager.addEventSwitchMenuDecor(this.button1, this.mc);
        EventManager.addEventSwitchToSimulation(this.button2, this.mc);
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
        b.setPrefHeight(30);

        b.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(b, Priority.ALWAYS);

        return b;
    }

    private void changeMenu(String titleString, MapTile defaultSelected, MapTile... mapTiles){
        this.container = new VBox();
        this.container.setSpacing(15);
        VBox.setVgrow(this.container, Priority.ALWAYS);

        BorderPane titleContainer = new BorderPane();
        Label titleLabel = new Label(titleString);

        //Rectangle des aides
        Rectangle help = new Rectangle(25,25);
        help.setMouseTransparent(false);
        help.setFill(new ImagePattern(new Image(getClass().getResource("/Image/Help.png").toExternalForm())));
        // Tooltip
        Tooltip t = new Tooltip("Sélectionner un objet et cliquer sur la grille pour le positionner");
        Tooltip.install(help, t);
        EventManager.addEventTooltipShow(help, t);
        EventManager.addEventTooltipHide(help, t);

        titleContainer.setLeft(titleLabel);
        titleContainer.setRight(help);
        this.container.getChildren().add(titleContainer);

        for(MapTile mt : mapTiles) {
            HBox selectContainer = new HBox();
            selectContainer.setSpacing(10);
            selectContainer.setAlignment(Pos.CENTER_LEFT);

            Rectangle imageSelect = new Rectangle(50, 50);
            imageSelect.setStroke(Color.BLACK);

            Label labelSelect = new Label(mt.getLabel());

            Image image = new Image(getClass().getResource(mt.getPathIcon()).toExternalForm());
            ImagePattern pattern = new ImagePattern(image);
            imageSelect.setFill(pattern);

            selectContainer.getChildren().addAll(imageSelect, labelSelect);

            this.container.getChildren().add(selectContainer);
            EventManager.addEventClickSelection(imageSelect, mt, this);

            if(mt.equals(defaultSelected)) {
                this.setSelected(imageSelect, defaultSelected);
            }
        }

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.BOTTOM_LEFT);
        buttonContainer.setSpacing(10);
        buttonContainer.getChildren().addAll(this.button1, this.button2);

        VBox.setVgrow(buttonContainer, Priority.ALWAYS);

        this.container.getChildren().add(buttonContainer);
    }

    public VBox getContainer() {
        return this.container;
    }
}
