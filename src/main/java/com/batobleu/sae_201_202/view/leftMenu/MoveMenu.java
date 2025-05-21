package com.batobleu.sae_201_202.view.leftMenu;

import com.batobleu.sae_201_202.controller.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MoveMenu {

    private final MainController mc;
    private final VBox container;

    public MoveMenu(MainController mc) {
        this.mc = mc;
        this.container = new VBox();
        this.container.setAlignment(Pos.TOP_CENTER);
        this.container.setSpacing(15);

        //Turn display
        Rectangle display = new Rectangle(80,80);
        display.setFill(new ImagePattern(new Image(getClass().getResource(MainController.Wolf.getPathIcon()).toExternalForm())));
        display.setStroke(Color.BLACK);

        Label moveLeft = new Label("Move Left : 0");
        Label round = new Label("Round : 0");

        GridPane containerArrow = new GridPane();
        containerArrow.setAlignment(Pos.CENTER);
        //containerArrow.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

        //Directional arrow
        createButtons(1, 0, "/image/LeftArrow.png", containerArrow);
        createButtons(1, 2, "/image/RightArrow.png", containerArrow);
        createButtons(0, 1, "/image/TopArrow.png", containerArrow);
        createButtons(2, 1, "/image/BottomArrow.png", containerArrow);

        //add to group
        this.container.getChildren().addAll(display, moveLeft, round, containerArrow);
    }

    private void createButtons(int y, int x, String pathImage, GridPane container) {
        StackPane sp = new StackPane();
        sp.setPrefSize(60, 60);
        sp.setAlignment(Pos.CENTER);

        Rectangle r = new Rectangle(50,50);
        r.setFill(new ImagePattern(new Image(getClass().getResource(pathImage).toExternalForm())));
        r.setStroke(Color.BLACK);

        sp.getChildren().addFirst(r);
        container.add(sp, x, y);
    }

    public VBox getContainer() {
        return this.container;
    }
}
