package com.batobleu.sae_201_202.view;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PopupsError {


    public void showError(String x){
        //stageErrror
        Stage stage = new Stage();
        VBox group = new VBox();
        group.setSpacing(10);
        Scene scene = new Scene(group,200,110);
        stage.setScene(scene);

        //FirstLabel
        Label label1 = new Label("Position invalide");
        label1.setFont(new Font("Arial", 15));
        group.getChildren().add(label1);

        //Line
        group.getChildren().add(new Line(100,100,300,100));

        //SecondeLabel
        Label label2 = new Label("Placement impossible pour\nl'objet " + x);
        label2.setFont(new Font("Arial", 15));
        group.getChildren().add(label2);

        //Button
        Button button = new Button("Ok");
        button.setTranslateX(100);
        button.setPrefHeight(30);
        button.setPrefWidth(110);
        button.setOnAction((ActionEvent e) ->{
            stage.close();
        });
        group.getChildren().add(button);


        //ShowTheError
        stage.show();
    }
}
