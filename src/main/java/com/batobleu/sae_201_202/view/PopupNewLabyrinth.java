package com.batobleu.sae_201_202.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupNewLabyrinth {

    public void popupNewLabyrinth(){
        showLabyrintheDialog();
    }

    private void showLabyrintheDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Nouveau Labyrinthe");

        Label label = new Label("Définissez la taille du labyrinthe:");

        Spinner<Integer> largeur = new Spinner<>(1, 100, 10);
        Spinner<Integer> hauteur = new Spinner<>(1, 100, 10);

        largeur.setEditable(true);
        hauteur.setEditable(true);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Largeur:"), 0, 0);
        grid.add(largeur, 1, 0);
        grid.add(new Label("Hauteur:"), 0, 1);
        grid.add(hauteur, 1, 1);

        Button okBtn = new Button("Ok");
        Button cancelBtn = new Button("Annuler");

        HBox buttonBox = new HBox(10, cancelBtn, okBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        VBox layout = new VBox(10, label, grid, buttonBox);
        layout.setPadding(new Insets(10));

        okBtn.setOnAction(e -> {
            int w = largeur.getValue();
            int h = hauteur.getValue();
            dialog.close();
        });

        cancelBtn.setOnAction(e -> dialog.close());

        dialog.setScene(new Scene(layout));
        dialog.showAndWait();
    }
}
