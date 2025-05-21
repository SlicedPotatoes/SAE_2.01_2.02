package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.view.Popup.PopupFileChooser;
import com.batobleu.sae_201_202.view.Popup.PopupsError;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MenuBarUp {
    private MainController mc;
    private MenuBar t;

    public MenuBarUp(MainController mc) {
        this.mc = mc;
        this.t = addMenuBar();
    }

    public MenuBar addMenuBar(){
        Menu fichier = new Menu("Fichier");
        Menu propos = new Menu("A Propos");

        MenuItem m1 = new MenuItem("Nouveau");
        MenuItem m2 = new MenuItem("Importer");
        MenuItem m3 = new MenuItem("Exporter");

        m1.setOnAction(e -> {
            this.mc.getStage().close();
            this.mc.start(new Stage());
        });
        m2.setOnAction(e -> {
            String pathString = new PopupFileChooser().getPath("Ouvrir", this.mc.getStage());

            if(pathString != null) {
                try {
                    Path path = Paths.get(pathString);
                    List<String> lines = null;
                    lines = Files.readAllLines(path);
                    this.mc.initWithFile(lines);
                }
                catch (IOException ex) {
                    new PopupsError(ex.toString()).show();
                }
            }
        });
        m3.setOnAction(e -> {
            InformationDebug.AddDebug("Pas encore implémenter ! ");
        });

        //à changer avec l'historique des labyrinthes
        Menu m4 = new Menu("Recent");
        MenuItem m5 = new MenuItem("C:\\...\\labyrinthe1.txt");
        m4.getItems().add(m5);

        m5.setOnAction(e -> {
            InformationDebug.AddDebug("Pas encore implémenter ! ");
        });

        fichier.getItems().addAll(m1,m2,m3,m4);

        MenuBar a = new MenuBar(fichier, propos);
        this.mc.getRoot().setTop(this.t);
        return a;
    }

}
