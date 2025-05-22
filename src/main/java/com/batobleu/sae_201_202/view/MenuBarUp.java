package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.entity.Sheep;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.view.Popup.PopupFileChooser;
import com.batobleu.sae_201_202.view.Popup.PopupsError;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class MenuBarUp {
    private static final String historyPath = "./history.txt";

    private MainController mc;
    private List<String> history;
    private Menu historyMenu;

    public MenuBarUp(MainController mc) {
        this.mc = mc;

        try {
            this.history = Files.readAllLines(Paths.get(historyPath));
        }
        catch (IOException e) {
            this.history = new ArrayList<>();
        }

        this.addMenuBar();
    }

    private void export() {
        String pathString = new PopupFileChooser().getPath("Enregistrer", this.mc.getStage(), PopupFileChooser.Type.Export);

        if(pathString != null) {
            StringBuilder strB = new StringBuilder();

            for(int y = 0; y < this.mc.getSimulation().getNy(); y++) {
                for(int x = 0; x < this.mc.getSimulation().getNx(); x++) {
                    Wolf wolf = this.mc.getSimulation().getWolf();
                    Sheep sheep = this.mc.getSimulation().getSheep();

                    MapTile mt;

                    if(wolf != null && wolf.getX() == x && wolf.getY() == y) {
                        mt = WOLF;
                    }
                    else if(sheep != null && sheep.getX() == x && sheep.getY() == y) {
                        mt = SHEEP;
                    }
                    else {
                        mt = this.mc.getSimulation().getMap()[y][x];
                    }

                    strB.append(MAP_TILE_CHARACTER_HASH_MAP.get(mt));
                }
                strB.append('\n');
            }

            byte[] strToBytes = strB.toString().getBytes();

            try {
                Files.write(Paths.get(pathString), strToBytes);
            }
            catch (IOException ex) {
                new PopupsError(ex.toString()).show();
            }
        }
    }

    private void addToHistory(String path) {
        StringBuilder strB = new StringBuilder();

        for(String s : history) {
            if(s.equals(path)) {
                return;
            }

            strB.append(s);
            strB.append('\n');
        }

        this.history.add(path);
        strB.append(path);

        byte[] strToBytes = strB.toString().getBytes();

        try {
            Files.write(Paths.get(historyPath), strToBytes);
            addMenuBar(); // Ajouter un nouveau MenuItem ne fonctionné pas
        }
        catch (IOException e) {
            new PopupsError(e.toString()).show();
        }
    }

    private void addMenuBar(){
        Menu fichier = new Menu("Fichier");
        Menu propos = new Menu("A Propos");

        MenuItem newButton = new MenuItem("Nouveau");
        MenuItem importButton = new MenuItem("Importer");
        MenuItem exportButton = new MenuItem("Exporter");

        newButton.setOnAction(e -> {
            this.mc.getStage().close();
            this.mc.start(new Stage());
        });

        importButton.setOnAction(e -> {
            String pathString = new PopupFileChooser().getPath("Ouvrir", this.mc.getStage(), PopupFileChooser.Type.Import);

            if(pathString != null) {
                try {
                    this.mc.initWithFile(Files.readAllLines(Paths.get(pathString)));
                    this.addToHistory(pathString);
                }
                catch (IOException ex) {
                    new PopupsError(ex.toString()).show();
                }
            }
        });

        exportButton.setOnAction(e -> {
            export();
        });

        this.historyMenu = new Menu("Recent");

        for(String path : history) {
            MenuItem m = new MenuItem(path);
            this.historyMenu.getItems().add(m);

            EventManager.addEventImportHistory(this.mc, m, path);
        }

        fichier.getItems().addAll(newButton, importButton, exportButton, this.historyMenu);

        MenuBar mb = new MenuBar(fichier, propos);
        this.mc.getRoot().setTop(mb);
    }

}
