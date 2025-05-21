package com.batobleu.sae_201_202.view.leftMenu;

import com.batobleu.sae_201_202.controller.MainController;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class LeftMenuManager {
    private MainController mc;
    private VBox container;

    public LeftMenuManager(MainController mc) {
        this.mc = mc;
        this.container = new VBox();

        this.container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
        this.container.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
        this.container.setPrefWidth(200);
        this.container.setPadding(new Insets(15));

        this.mc.getRoot().setLeft(this.container);
    }

    public void showMenuDecor() {
        this.container.getChildren().remove(this.mc.getMoveMenu().getContainer());
        this.container.getChildren().remove(this.mc.getMsi().getContainer());

        this.mc.getMsi().switchToMenuDecor();

        this.container.getChildren().add(this.mc.getMsi().getContainer());
    }

    public void showMenuEntity() {
        this.container.getChildren().remove(this.mc.getMoveMenu().getContainer());
        this.container.getChildren().remove(this.mc.getMsi().getContainer());

        this.mc.getMsi().switchToMenuEntity();

        this.container.getChildren().add(this.mc.getMsi().getContainer());
    }

    public void showMenuMove() {
        this.container.getChildren().remove(this.mc.getMsi().getContainer());
        this.container.getChildren().add(this.mc.getMoveMenu().getContainer());
    }


}
