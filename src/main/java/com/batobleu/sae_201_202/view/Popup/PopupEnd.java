package com.batobleu.sae_201_202.view.Popup;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.tile.MapTile;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;

// Popup de fin de partie, affichant le gagnant et les statistiques de la partie
public class PopupEnd extends Alert {
    public PopupEnd(MainController mc) {
        super(AlertType.INFORMATION);
        super.setTitle("Fin de partie");
        super.setHeaderText("Statistique de fin de partie");

        DialogPane dp = this.getDialogPane();

        // Container
        VBox container = new VBox();
        container.setSpacing(10);

        // Label pour le gagnant et le nombre de round
        Label winner = new Label("Gagnant: " + (mc.getSimulation().getSheep().getIsSafe() ? "Mouton" : (mc.getSimulation().getSheep().getIsEaten() ? "Loup" : "Match nul")));
        Label round = new Label("Nombre de tour: " + mc.getSimulation().getCurrRound());

        // Ajout des labels au container
        container.getChildren().addAll(winner, round);

        // Parcours des différents compteurs et affiche les informations
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
