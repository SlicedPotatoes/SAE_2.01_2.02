package com.batobleu.sae_201_202.view.leftMenu;

import com.batobleu.sae_201_202.controller.MainController;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

// Classe permettant la gestion de l'affichage des menus sur la gauche
public class LeftMenuManager {
    private final MainController mc;
    private final VBox container;

    public LeftMenuManager(MainController mc) {
        this.mc = mc;
        this.container = new VBox();

        this.container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
        this.container.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
        this.container.setPrefWidth(200);
        this.container.setPadding(new Insets(15));

        this.mc.getRoot().setLeft(this.container);
    }

    // Affiche le menu de sélection de décor
    public void showMenuDecor() {
        this.container.getChildren().remove(this.mc.getMoveMenu().getContainer());
        this.container.getChildren().remove(this.mc.getMsi().getContainer());

        this.mc.getMsi().switchToMenuDecor();

        this.container.getChildren().add(this.mc.getMsi().getContainer());
    }

    // Affiche le menu de sélection d'entité
    public void showMenuEntity() {
        this.container.getChildren().remove(this.mc.getMoveMenu().getContainer());
        this.container.getChildren().remove(this.mc.getMsi().getContainer());

        this.mc.getMsi().switchToMenuEntity();

        this.container.getChildren().add(this.mc.getMsi().getContainer());
    }

    // Affiche le menu des contrôles manuels
    public void showMenuMove() {
        this.container.getChildren().remove(this.mc.getMsi().getContainer());
        this.container.getChildren().add(this.mc.getMoveMenu().getContainer());
    }
}
