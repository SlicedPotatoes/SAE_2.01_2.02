package com.batobleu.sae_201_202.view.Popup;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PopupFileChooser {
    public enum Type {
        Import,
        Export
    }

    public PopupFileChooser() { }

    public String getPath(String title, Stage s, Type t) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = t == Type.Import ? fileChooser.showOpenDialog(s) : fileChooser.showSaveDialog(s);

        if(selectedFile != null) {
            return selectedFile.getPath();
        }

        return null;
    }
}
