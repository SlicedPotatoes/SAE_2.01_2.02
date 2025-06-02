package com.batobleu.sae_201_202.view.leftMenu;

import com.batobleu.sae_201_202.controller.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static com.batobleu.sae_201_202.controller.MainController.SHEEP;

public class AutoMenu {
    private final MainController mc;
    private final VBox container;

    private final Label moveLeft;
    private final Label round;
    private final Rectangle display;

    public AutoMenu(MainController mc){
        this.mc = mc;
        this.container = new VBox();
        this.container.setSpacing(15);

        Label title = new Label("Gérer la simulation");

        HBox infoContainer = new HBox();
        infoContainer.setSpacing(5);

        // Affichage de l'entité a qui c'est le tour
        this.display = new Rectangle(40,40);
        this.display.setFill(new ImagePattern(new Image(getClass().getResource(SHEEP.getPathIcon()).toExternalForm())));
        this.display.setStroke(Color.BLACK);

        VBox labelContainer = new VBox();
        labelContainer.setSpacing(5);
        labelContainer.setAlignment(Pos.CENTER_LEFT);
        // Label du tour actuel
        this.round = new Label();
        this.round.setText("Tour n° " + this.mc.getSimulation().getCurrRound());
        // Label des mouvements restants
        this.moveLeft = new Label();
        this.moveLeft.setText("Mouvement restant : " + this.mc.getSimulation().getMoveLeft());
        labelContainer.getChildren().addAll(this.round, this.moveLeft);

        infoContainer.getChildren().addAll(this.display, labelContainer);

        // Controle de la simulation
        BorderPane arrowContainer = new BorderPane();

        // Fleche "précédente"
        HBox leftArrowContainer = new HBox();
        leftArrowContainer.setAlignment(Pos.CENTER);
        leftArrowContainer.setSpacing(5);
        Button leftButton = new Button("<");
        Label leftLabel = new Label("Précédent");
        leftArrowContainer.getChildren().addAll(leftButton, leftLabel);

        // Fleche "suivante"
        HBox rightArrowContainer = new HBox();
        rightArrowContainer.setAlignment(Pos.CENTER);
        rightArrowContainer.setSpacing(5);
        Label rightLabel = new Label("Suivant");
        Button rightButton = new Button(">");
        rightArrowContainer.getChildren().addAll(rightLabel, rightButton);

        arrowContainer.setLeft(leftArrowContainer);
        arrowContainer.setRight(rightArrowContainer);

        CheckBox cbAuto = new CheckBox("Défilement automatique");
        Slider sliderSpeed = new Slider(0, 100, 50);
        sliderSpeed.setShowTickLabels(true);
        sliderSpeed.setMajorTickUnit(50);
        sliderSpeed.setBlockIncrement(10);

        this.container.getChildren().addAll(title, infoContainer, arrowContainer, cbAuto, sliderSpeed);
    }

    public VBox getContainer() {
        return this.container;
    }
}
