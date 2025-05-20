package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.exception.InvalidPositionException;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.view.MenuSelectItems;
import com.batobleu.sae_201_202.view.PopupTypeSimulation;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EventManager {

    /*
       Evenement de la classe Map
     */

    // Ajout des événements pour détecter un changement d'éléments sélectionnés
    public static void addValidPositionEventOnMap(MainController mc) {
        mc.getMsi().currSelectedProperty().addListener((observable, oldValue, newValue) -> {
            // Permet de ne pas faire d'Exception, quand on force le trigger "manuellement" avec apres un appuis sur le bouton reset
            if(newValue == null) {
                return;
            }

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

    // Ajout des événements pour modifier la map et la simuation lors de la configuration de celle ci.
    // TO DO: En mode "Control Manuel / Automatique", désactiver les événements
    public static void addEventOnClickMap(MainController mc) {
        for (int y = 0; y < mc.getSimulation().getNy(); y++) {
            for(int x = 0; x < mc.getSimulation().getNx(); x++) {
                int _x = x, _y = y;

                mc.getMap().getImages()[y][x].setOnMouseClicked((MouseEvent e) -> {
                    MapTile selectedItem = mc.getMsi().currSelectedProperty().get();
                    try {
                        mc.updateMapAndSimulation(_x, _y, selectedItem);
                    }
                    catch (InvalidPositionException ex) {
                        System.out.println(ex);
                    }
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
    public static void addEventSwitchMenuEntity(Button b, MenuSelectItems msi) {
        b.setOnAction((ActionEvent e) -> {
            msi.switchToMenuEntity();
        });
    }

    // Ajout de l'événement pour le bouton "Retour" sur le menu "Entité"
    public static void addEventSwitchMenuDecor(Button b, MenuSelectItems msi) {
        b.setOnAction((ActionEvent e) -> {
            msi.switchToMenuDecor();
        });
    }

    // Ajout de l'événement pour le bouton "Valider" sur le menu "Entité"
    public static void addEventSwitchToSimulation(Button b) {
        b.setOnAction((ActionEvent e) -> {
            PopupTypeSimulation p = new PopupTypeSimulation();
            p.PopupTypeSimulation();
        });
    }

    // Ajout de l'événement permettant de changer l'élément sélectionner
    public static void addEventClickSelection(Rectangle r, MapTile mt, MenuSelectItems msi) {
        r.setOnMouseClicked((MouseEvent e) -> {
            msi.setSelected(r, mt);
        });
    }

    public static void addEventTooltipShow(Rectangle r, Tooltip t) {
        r.setOnMouseEntered((MouseEvent e) -> {
            t.show(r, e.getScreenX(), e.getScreenY() - 50);
        });
    }

    public static void addEventTooltipHide(Rectangle r, Tooltip t) {
        r.setOnMouseExited((MouseEvent e) -> {
            t.hide();
        });
    }
}
