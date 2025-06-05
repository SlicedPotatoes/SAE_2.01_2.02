package com.batobleu.sae_201_202.view.Popup;

import com.batobleu.sae_201_202.model.SettingsAutoSimulation;
import com.batobleu.sae_201_202.model.algo.PathFinding;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class PopupSettingAutoSimulation extends Dialog<SettingsAutoSimulation> {
    public PopupSettingAutoSimulation() {
        super();
        super.setTitle("Configuration simulation automatique");
        super.setHeaderText(null);

        // Création des types de buttons pour savoir si l'utilisateur a annulé ou validé les données saisies
        ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        super.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        // Container
        GridPane container = new GridPane();
        container.setHgap(10);
        container.setVgap(10);
        container.setPadding(new Insets(20));

        // Input
        Spinner<Integer> dManhattan = new Spinner<>(0, 999, 5);
        dManhattan.setEditable(true);

        ChoiceBox<String> algoSheep = new ChoiceBox<>();
        algoSheep.getItems().addAll(SHEEP_ALGORITHM);
        algoSheep.setValue("Random");

        ChoiceBox<String> algoWolf = new ChoiceBox<>();
        algoWolf.getItems().addAll(WOLF_ALGORITHM);
        algoWolf.setValue("Random");

        container.add(new Label("Distance de Manhattan"), 0, 0);
        container.add(dManhattan, 1, 0);
        container.add(new Label("Algorithme Mouton"), 0, 1);
        container.add(algoSheep, 1, 1);
        container.add(new Label("Algorithme Loup"), 0, 2);
        container.add(algoWolf, 1, 2);

        super.getDialogPane().setContent(container);

        // Retourne le résultat si l'utilisateur a validé la saisie, sinon null
        super.setResultConverter(dialogButton -> {
            if(dialogButton == okButtonType) {
                PathFinding sheepAlgo = STRING_ALGORITHM_HASHMAP.getOrDefault(algoSheep.getValue(), STRING_ALGORITHM_HASHMAP.get("Random"));
                PathFinding wolfAlgo = STRING_ALGORITHM_HASHMAP.getOrDefault(algoWolf.getValue(), STRING_ALGORITHM_HASHMAP.get("Random"));
                return new SettingsAutoSimulation(dManhattan.getValue(), sheepAlgo, wolfAlgo);
            }
            return null;
        });
    }
}
