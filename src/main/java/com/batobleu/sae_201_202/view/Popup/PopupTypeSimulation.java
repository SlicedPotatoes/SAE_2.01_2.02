package com.batobleu.sae_201_202.view.Popup;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.view.CurrPage;
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

    public PopupTypeSimulation(MainController mc){
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Choix du type de simulation");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        Button manual = new Button("Manuelle");
        manual.setPrefWidth(100);

        Button auto = new Button("Automatique");
        auto.setPrefWidth(100);

        HBox buttonBox = new HBox(10, manual, auto);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(grid, buttonBox);
        layout.setPadding(new Insets(10));

        manual.setOnAction(e -> {
            mc.setCurrPage(CurrPage.ControlManual);
            dialog.close();
        });

        auto.setOnAction(e -> {
            InformationDebug.AddDebug("Attendre la 2.02 pour avoir cette fonctionnalité");
            dialog.close();
        });

        dialog.setScene(new Scene(layout));
        dialog.showAndWait();
    }

}
