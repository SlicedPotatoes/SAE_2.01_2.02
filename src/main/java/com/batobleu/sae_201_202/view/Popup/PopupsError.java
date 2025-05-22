package com.batobleu.sae_201_202.view.Popup;

import javafx.scene.control.Alert;

// Classe pour afficher un popup d'erreur
public class PopupsError extends Alert {

    public PopupsError(String s) {
        super(AlertType.ERROR);
        super.setTitle("Erreur");
        super.setHeaderText(null);
        super.setContentText(s);
        super.showAndWait();
    }

}
