package com.batobleu.sae_201_202.view.Popup;

import com.batobleu.sae_201_202.view.InformationDebug;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupTypeSimulation {

    public void PopupTypeSimulation(){
        showTypeSimulationDialog();
    }


    private void showTypeSimulationDialog() {
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Choix du type de simulation");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        Button manual = new Button("manuelle");
        Button auto = new Button("automatique");
        Button annuler = new Button("annuler");

        HBox buttonBox = new HBox(10, manual, auto, annuler);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(grid, buttonBox);
        layout.setPadding(new Insets(10));

        manual.setOnAction(e -> {
            dialog.close();
        });

        auto.setOnAction(e -> {
            InformationDebug.AddDebug("Attendre la 2.02 pour avoir cette fonctionnalité");
            dialog.close();
        });

        annuler.setOnAction(e ->{
            dialog.close();
        });

        dialog.setScene(new Scene(layout));
        dialog.showAndWait();
    }
}
