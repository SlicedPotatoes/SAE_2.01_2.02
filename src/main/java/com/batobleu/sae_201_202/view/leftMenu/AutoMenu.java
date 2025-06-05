package com.batobleu.sae_201_202.view.leftMenu;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.entity.Sheep;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.view.EventManager;
import com.batobleu.sae_201_202.view.Popup.PopupEnd;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static com.batobleu.sae_201_202.controller.MainController.*;
import static javafx.scene.input.MouseEvent.*;

public class AutoMenu {
    private final MainController mc;
    private final Scene scene;
    private final VBox container;


    private final Label moveLeft;
    private final Label round;
    private final Rectangle display;

    public AutoMenu(MainController mc, Scene scene){
        this.mc = mc;
        this.scene = scene;
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

        this.scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.E)){
                this.update(false);
            }
            if (e.getCode().equals(KeyCode.A)){
                this.update(true);
            }
        });

        leftButton.setOnAction(e -> {
            this.update(true);
        });

        rightButton.setOnAction(e -> {
            this.update(false);
        });


    }

    private void update(boolean prev) {
        Wolf w = this.mc.getSimulation().getWolf();
        Sheep s = this.mc.getSimulation().getSheep();

        this.mc.getMap().updateImage(w.getX(), w.getY(), this.mc.getSimulation().getMap()[w.getY()][w.getX()]);
        this.mc.getMap().updateImage(s.getX(), s.getY(), this.mc.getSimulation().getMap()[s.getY()][s.getX()]);

        if(prev) {
            this.mc.getSimulation().getPrev();
        }
        else {
            this.mc.getSimulation().getNext();
            if(this.mc.getSimulation().isEnd()) {
                new PopupEnd(this.mc);
            }
        }

        w = this.mc.getSimulation().getWolf();
        s = this.mc.getSimulation().getSheep();

        this.mc.getMap().updateImage(s.getX(), s.getY(), SHEEP);
        this.mc.getMap().updateImage(w.getX(), w.getY(), WOLF);

        // Mets à jour les différents labels, et l'icône de l'entité à qui c'est le tour.
        this.moveLeft.setText("Mouvement restant : " + this.mc.getSimulation().getMoveLeft());
        this.round.setText("Tour n° " + this.mc.getSimulation().getCurrRound());
        this.display.setFill(new ImagePattern(new Image(getClass().getResource(this.mc.getSimulation().getCurrEntityTurn().getPathIcon()).toExternalForm())));
    }

    public VBox getContainer() {
        return this.container;
    }
}
