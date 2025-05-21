package com.batobleu.sae_201_202.view.leftMenu;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.exception.InvalidPositionException;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.view.EventManager;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class MoveMenu {

    private final MainController mc;
    private final VBox container;

    private final Label moveLeft;
    private final Label round;
    private final Rectangle display;

    public MoveMenu(MainController mc) {
        this.mc = mc;
        this.container = new VBox();
        this.container.setAlignment(Pos.TOP_CENTER);
        this.container.setSpacing(15);

        //Turn display
        this.display = new Rectangle(80,80);
        this.display.setFill(new ImagePattern(new Image(getClass().getResource(SHEEP.getPathIcon()).toExternalForm())));
        this.display.setStroke(Color.BLACK);

        this.moveLeft = new Label();
        this.moveLeft.setText("Mouvement restant : " + this.mc.getSimulation().getMoveLeft());

        this.round = new Label();
        this.round.setText("Tour n° " + this.mc.getSimulation().getCurrRound());

        GridPane containerArrow = new GridPane();
        containerArrow.setAlignment(Pos.CENTER);
        //containerArrow.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

        //Directional arrow
        createButtons(1, 0, "/image/LeftArrow.png", containerArrow, 'l');
        createButtons(1, 2, "/image/RightArrow.png", containerArrow, 'r');
        createButtons(0, 1, "/image/TopArrow.png", containerArrow, 'u');
        createButtons(2, 1, "/image/BottomArrow.png", containerArrow, 'd');

        //add to group
        this.container.getChildren().addAll(display, this.moveLeft, this.round, containerArrow);
    }

    private void createButtons(int y, int x, String pathImage, GridPane container, char dir) {
        StackPane sp = new StackPane();
        sp.setPrefSize(60, 60);
        sp.setAlignment(Pos.CENTER);

        Rectangle r = new Rectangle(50,50);
        r.setFill(new ImagePattern(new Image(getClass().getResource(pathImage).toExternalForm())));
        r.setStroke(Color.BLACK);

        sp.getChildren().addFirst(r);
        container.add(sp, x, y);

        EventManager.addEventMove(this.mc, sp, dir);
    }

    public VBox getContainer() {
        return this.container;
    }

    public void update(char dir) throws InvalidPositionException {
        MapTile mt = this.mc.getSimulation().getCurrEntityTurn();
        Entity e = mt == WOLF ? this.mc.getSimulation().getWolf() : this.mc.getSimulation().getSheep();

        if(e.getX() == this.mc.getSimulation().getWolf().getX() && e.getY() == this.mc.getSimulation().getWolf().getY()) {
            this.mc.getMap().updateImage(e.getX(), e.getY(), WOLF);
        }
        else {
            this.mc.getMap().updateImage(e.getX(), e.getY(), mt);
        }

        int prevX = e.getX() + CHARACTER_DIRECTION.get(dir).getKey() * -1;
        int prevY = e.getY() + CHARACTER_DIRECTION.get(dir).getValue() * -1;

        this.mc.getMap().updateImage(prevX, prevY, this.mc.getSimulation().getMap()[prevY][prevX]);

        this.mc.getSimulation().endTurn();

        this.moveLeft.setText("Mouvement restant : " + this.mc.getSimulation().getMoveLeft());
        this.round.setText("Tour n° " + this.mc.getSimulation().getCurrRound());

        this.display.setFill(new ImagePattern(new Image(getClass().getResource(this.mc.getSimulation().getCurrEntityTurn().getPathIcon()).toExternalForm())));
    }
}
