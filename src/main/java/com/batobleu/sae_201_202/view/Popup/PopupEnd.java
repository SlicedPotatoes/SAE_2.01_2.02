package com.batobleu.sae_201_202.view.Popup;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.tile.MapTile;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;

public class PopupEnd extends Alert {
    public PopupEnd(MainController mc) {
        super(AlertType.INFORMATION);
        super.setTitle("Fin de partie");
        super.setHeaderText("Statistique de fin de partie");

        DialogPane dp = this.getDialogPane();

        VBox container = new VBox();
        container.setSpacing(10);

        Label winner = new Label("Gagnant: " + (mc.getSimulation().getSheep().getIsSafe() ? "Mouton" : "Loup"));
        Label round = new Label("Nombre de tour: " + mc.getSimulation().getCurrRound());

        container.getChildren().addAll(winner, round);

        for(Map.Entry<MapTile, Integer> entry : mc.getSimulation().getCounts().entrySet()) {
            MapTile mt = entry.getKey();
            int count = entry.getValue();

            Label l = new Label(mt.getLabel() + " mangé: " + count);
            container.getChildren().add(l);
        }

        dp.setContent(container);

        super.showAndWait();
    }
}
