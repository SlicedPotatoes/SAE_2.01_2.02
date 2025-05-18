package com.batobleu.sae_201_202.view;

import javafx.scene.Group;
import javafx.scene.control.*;

public class MenuBarUp {
    private Group root;
    private MenuBar t;


    public MenuBarUp(Group root) {
        this.root = root;
        this.t = addMenuBar();
    }


    public MenuBar addMenuBar(){
        Menu Fichier = new Menu("Fichier");
        Menu propos = new Menu("A Propos");

        MenuItem m1 = new MenuItem("Nouveau");
        MenuItem m2 = new MenuItem("Importer");
        MenuItem m3 = new MenuItem("Exporter");


        //à changer avec l'historique des labyrinthe
        Menu m4 = new Menu("Recent");
        MenuItem m5 = new MenuItem("C:\\...\\labyrinthe1.txt");
        m4.getItems().add(m5);


        Fichier.getItems().addAll(m1,m2,m3,m4);

        MenuBar a = new MenuBar(Fichier, propos);
        root.getChildren().add(a);
        return a;
    }

}
