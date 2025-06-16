package com.batobleu.sae_201_202.view.Popup;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.SettingsAutoSimulation;
import com.batobleu.sae_201_202.model.exception.IllegalMoveException;
import com.batobleu.sae_201_202.view.CurrPage;
import com.batobleu.sae_201_202.view.InformationDebug;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

// Classe affichant un popup laissant le choix à l'utilisateur d'ouvrir la simulation manuel ou automatique
public class PopupTypeSimulation {

    public PopupTypeSimulation(MainController mc){
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Choix du type de simulation");

        // Button
        Button manual = new Button("Manuel");
        manual.setPrefWidth(100);
        Button auto = new Button("Automatique");
        auto.setPrefWidth(100);

        // Container
        HBox container = new HBox(10, manual, auto);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        // Event sur les boutons
        manual.setOnAction(e -> {
            mc.setCurrPage(CurrPage.ControlManual);
            dialog.close();
        });
        auto.setOnAction(e -> {
            PopupSettingAutoSimulation p = new PopupSettingAutoSimulation();
            Optional<SettingsAutoSimulation> result = p.showAndWait();

            if(result.isEmpty()) {
                return;
            }

            mc.setCurrPage(CurrPage.ControlAuto);
            try {
                mc.getSimulation().autoSimulation(result.get().getDManhattan(), result.get().getAlgoSheep(), result.get().getAlgoWolf(), result.get().getVision());
            } catch (IllegalMoveException ex) {
                InformationDebug.AddDebug(ex.toString());
            }
            dialog.close();
        });

        dialog.setScene(new Scene(container));
        dialog.showAndWait();
    }

}
