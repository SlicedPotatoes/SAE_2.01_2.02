package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.model.exception.IllegalMoveException;
import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.exception.InvalidPositionException;
import com.batobleu.sae_201_202.model.exception.invalidMap.*;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.view.Popup.PopupEnd;
import com.batobleu.sae_201_202.view.Popup.PopupTypeSimulation;
import com.batobleu.sae_201_202.view.Popup.PopupsError;
import com.batobleu.sae_201_202.view.leftMenu.MenuSelectItems;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EventManager {

    /*
       Evenement de la classe Map
     */

    // Ajout des événements pour détecter un changement d'éléments sélectionnés
    public static void addValidPositionEventOnMap(MainController mc) {
        mc.getMsi().currSelectedProperty().addListener((observable, oldValue, newValue) -> {
            // Permet de ne pas faire d'Exception, quand on force le trigger "manuellement" avec après un appui sur le bouton reset
            if(newValue == null) {
                return;
            }

            // Met a jour les indicateurs de position valide
            for(int y = 0; y < mc.getSimulation().getNy(); y++) {
                for(int x = 0; x < mc.getSimulation().getNx(); x++) {
                    if(newValue.isValidPosition(x, y, mc.getSimulation().getNx(), mc.getSimulation().getNy(), mc.getSimulation().getMap()[y][x])) {
                        mc.getMap().getValidPositionIndicator(x, y).setFill(new Color(0, 0.78, 0.33, 0.5));
                    }
                    else {
                        mc.getMap().getValidPositionIndicator(x, y).setFill(new Color(0.83, 0.18, 0.18, 0.5));
                    }
                }
            }
        });
    }

    // Ajout des événements pour modifier la map avec et la simulation avec l'élément sélectionné.
    private static void actionOnClickMap(MainController mc, int x, int y) {
        // Mettre à jour la map, au click seulement dans le cas de la configuration du labyrinthe
        if(mc.getCurrPage().get() != CurrPage.SetupDecor && mc.getCurrPage().get() != CurrPage.SetupEntity) {
            return;
        }

        MapTile selectedItem = mc.getMsi().currSelectedProperty().get();
        try {
            mc.updateMapAndSimulation(x, y, selectedItem);
        }
        catch (InvalidPositionException ex) {
            System.out.println(ex);
        }
    }
    public static void addEventOnClickMap(MainController mc) {
        for (int y = 0; y < mc.getSimulation().getNy(); y++) {
            for(int x = 0; x < mc.getSimulation().getNx(); x++) {
                int _x = x, _y = y;
                // La hitbox de l'image est plus petite que la hitbox réel.
                // On ajoute l'événement a l'image et a l'indicateur de position valide
                mc.getMap().getValidPositionIndicator(x, y).setOnMouseClicked((MouseEvent e) -> {
                    actionOnClickMap(mc, _x, _y);
                });
                mc.getMap().getImages()[y][x].setOnMouseClicked((MouseEvent e) -> {
                    actionOnClickMap(mc, _x, _y);
                });
            }
        }
    }

    /*
        Evenement de la classe MenuSelectItem
     */

    // Ajout de l'événement pour le bouton "Reset" sur le menu "Décor"
    public static void addEventResetButton(Button b, MainController mc, ObjectProperty<MapTile> currSelected) {
        b.setOnAction((ActionEvent e) -> {
            mc.getSimulation().init();
            mc.getMap().addMap();

            MapTile temp = currSelected.get();
            currSelected.set(null);
            currSelected.set(temp);
        });
    }

    // Ajout de l'événement pour le bouton "Suivant" sur le menu "Décor"
    public static void addEventSwitchMenuEntity(Button b, MainController mc) {
        b.setOnAction((ActionEvent e) -> {
            mc.setCurrPage(CurrPage.SetupEntity);
        });
    }

    // Ajout de l'événement pour le bouton "Retour" sur le menu "Entité"
    public static void addEventSwitchMenuDecor(Button b, MainController mc) {
        b.setOnAction((ActionEvent e) -> {
            mc.setCurrPage(CurrPage.SetupDecor);
        });
    }

    // Ajout de l'événement pour le bouton "Valider" sur le menu "Entité"
    // Vérifie si le labyrinthe est valide, affiche un message dans le cas ou il ne l'est pas.
    public static void addEventSwitchToSimulation(Button b, MainController mc) {
        b.setOnAction((ActionEvent e) -> {
            try {
                mc.getSimulation().isValidMap();
                new PopupTypeSimulation(mc);
            }
            catch (NoExitException ex) {
                new PopupsError("Le labyrinthe doit comporter une sortie");
                InformationDebug.AddDebug(ex.toString());
            }
            catch (NoWolfException ex) {
                new PopupsError("Un loup doit être présent dans le labyrinthe");
                InformationDebug.AddDebug(ex.toString());
            }
            catch (NoSheepException ex) {
                new PopupsError("Un mouton doit être présent dans le labyrinthe");
                InformationDebug.AddDebug(ex.toString());
            }
            catch (UnconnectedGraphException ex) {
                new PopupsError("Toute case doit être accessible depuis n'importe quelle autre");
                InformationDebug.AddDebug(ex.toString());
            }
            catch (InvalidMapException ex) {
                new PopupsError(ex.toString());
                InformationDebug.AddDebug(ex.toString());
            }
        });
    }

    // Ajout de l'événement permettant de changer l'élément sélectionner
    public static void addEventClickSelection(Rectangle r, MapTile mt, MenuSelectItems msi) {
        r.setOnMouseClicked((MouseEvent e) -> {
            msi.setSelected(r, mt);
        });
    }

    // Event pour afficher un tooltip sur un rectangle
    public static void addEventTooltipShow(Rectangle r, Tooltip t) {
        r.setOnMouseEntered((MouseEvent e) -> {
            t.show(r, e.getScreenX(), e.getScreenY() - 50);
        });
    }

    // Event pour cacher un tooltip
    public static void addEventTooltipHide(Rectangle r, Tooltip t) {
        r.setOnMouseExited((MouseEvent e) -> {
            t.hide();
        });
    }

    // Gestion des pages à afficher
    public static void addEventChangePage(MainController mc) {
        mc.getCurrPage().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case SetupDecor:
                    mc.getLmm().showMenuDecor();
                    break;
                case SetupEntity:
                    mc.getLmm().showMenuEntity();
                    break;
                case ControlManual:
                    mc.getLmm().showMenuMove();
                    break;
                case ControlAuto:
                    // TO DO
                    break;
            }
        });
    }

    // Event gestion mouvement dans simulation manuelle
    public static void addEventMove(MainController mc, StackPane sp, char dir) {
        sp.setOnMouseClicked((MouseEvent e) -> {
            if(mc.getSimulation().isEnd()) {
                return;
            }

            try {
                mc.getSimulation().move(dir);
                mc.getMoveMenu().update(dir);

                if(mc.getSimulation().isEnd()) {
                    new PopupEnd(mc);
                }
            }
            catch (IllegalMoveException | InvalidPositionException ex) {
                InformationDebug.AddDebug(ex.toString());
            }
        });
    }

    // Click sur un bouton de l'historique, permet charger la configuration
    public static void addEventImportHistory(MainController mc, MenuItem m, String path) {
        m.setOnAction((ActionEvent e) -> {
            try {
                mc.initWithFile(Files.readAllLines(Paths.get(path)));
            }
            catch (IOException ex) {
                new PopupsError(ex.toString()).show();
            }
        });
    }
}
