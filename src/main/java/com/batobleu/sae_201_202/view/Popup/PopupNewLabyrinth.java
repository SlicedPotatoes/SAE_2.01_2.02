package com.batobleu.sae_201_202.view.Popup;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

// Classe permettant d'afficher le popup pour configurer la taille d'un nouveau labyrinthe
public class PopupNewLabyrinth extends Dialog<Pair<Integer, Integer>> {
    public PopupNewLabyrinth(){
        super();
        super.setTitle("Nouveau Labyrinthe");
        super.setHeaderText(null);

        // Création des types de buttons pour savoir si l'utilisateur a annulé ou validé les données saisies
        ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        super.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        // Container
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Input
        Spinner<Integer> width = new Spinner<>(4, 15, 10);
        Spinner<Integer> height = new Spinner<>(4, 15, 10);
        width.setEditable(true);
        height.setEditable(true);

        // Ajout des inputs au container
        grid.add(new Label("Largeur:"), 0, 0);
        grid.add(width, 1, 0);
        grid.add(new Label("Hauteur:"), 0, 1);
        grid.add(height, 1, 1);

        super.getDialogPane().setContent(grid);

        // Retourne le résultat si l'utilisateur a validé la saisie, sinon null
        super.setResultConverter(dialogButton -> {
            if(dialogButton == okButtonType) {
                return new Pair<>(width.getValue(), height.getValue());
            }
            return null;
        });
    }
}
