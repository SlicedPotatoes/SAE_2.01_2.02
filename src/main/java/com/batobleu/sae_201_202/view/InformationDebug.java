package com.batobleu.sae_201_202.view;

import com.batobleu.sae_201_202.controller.MainController;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class InformationDebug {

    private MainController controller;
    private static TextArea bugs;

    public InformationDebug(MainController controller) {
        this.controller = controller;

        StackPane container = new StackPane();
        container.prefWidthProperty().bind(this.controller.getStage().widthProperty().multiply(0.25));

        bugs = init(container);
        this.controller.getRoot().setRight(container);
    }

    public TextArea init(StackPane g){
        TextArea t = new TextArea("Information Debug\n\n");

        t.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        t.setScrollTop(1);
        t.setEditable(false);

        g.getChildren().add(t);
        return t;
    }

    public static void AddDebug(String bug){
        bugs.appendText(bug + "\n\n");
    }
}
