package com.batobleu.sae_201_202.view.Popup;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class PopupNewLabyrinth extends Dialog<Pair<Integer, Integer>> {
    public PopupNewLabyrinth(){
        super();

        super.setTitle("Nouveau Labyrinthe");
        super.setHeaderText(null);

        ButtonType okButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        super.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Spinner<Integer> width = new Spinner<>(4, 15, 10);
        Spinner<Integer> height = new Spinner<>(4, 15, 10);
        width.setEditable(true);
        height.setEditable(true);

        grid.add(new Label("Largeur:"), 0, 0);
        grid.add(width, 1, 0);
        grid.add(new Label("Hauteur:"), 0, 1);
        grid.add(height, 1, 1);

        super.getDialogPane().setContent(grid);

        super.setResultConverter(dialogButton -> {
            if(dialogButton == okButtonType) {
                return new Pair<>(width.getValue(), height.getValue());
            }
            return null;
        });
    }
}
