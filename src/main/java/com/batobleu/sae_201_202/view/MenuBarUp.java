package com.batobleu.sae_201_202.view;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class MenuBarUp {
    private BorderPane root;
    private MenuBar t;

    public MenuBarUp(BorderPane root) {
        this.root = root;
        this.t = addMenuBar();
    }

    public MenuBar addMenuBar(){
        Menu fichier = new Menu("Fichier");
        Menu propos = new Menu("A Propos");

        MenuItem m1 = new MenuItem("Nouveau");
        MenuItem m2 = new MenuItem("Importer");
        MenuItem m3 = new MenuItem("Exporter");

        m1.setOnAction(e -> {
            InformationDebug.AddDebug("Pas encore implémenter ! ");
        });
        m2.setOnAction(e -> {
            InformationDebug.AddDebug("Pas encore implémenter ! ");
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
        root.setTop(this.t);
        return a;
    }

}
