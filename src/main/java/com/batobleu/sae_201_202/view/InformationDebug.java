package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.MainController;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InformationDebug {

    private MainController controller;
    private static TextArea bugs;

    public InformationDebug(MainController controller) {
        this.controller = controller;
        bugs = init();
        this.controller.getRoot().getChildren().add(bugs);
    }

    public TextArea init(){
        TextArea t = new TextArea("Information Debug\n\n");
        t.setTranslateX(950);
        t.setTranslateY(50);
        t.setPrefSize(300,510);
        t.setScrollTop(1);
        t.setEditable(false);
        return t;
    }



    public static void AddDebug(String bug){
        bugs.appendText(bug + "\n\n");
    }

}
