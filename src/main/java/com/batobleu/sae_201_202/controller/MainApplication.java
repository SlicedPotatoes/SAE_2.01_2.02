package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.model.Case;
import com.batobleu.sae_201_202.model.Simulation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group g = new Group();
        Scene scene = new Scene(g, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void afficherMap(Simulation s){
        StringBuilder out = new StringBuilder();
        String str = "";
        for (int i = 0; i < s.getNy(); i++) {
            str += "+---";
        }
        str += "+\n";
        out.append(str);
        for(int i = 0; i < s.getMap().length; i++){
            out.append("| ");
            for(int j = 0; j < s.getMap()[0].length; j++){
                if (s.getMap()[i][j] == Case.Grass) {
                    out.append("\033[92mG\033[0m");
                }
                else if (s.getMap()[i][j] == Case.Poppy) {
                    out.append("\033[91mP\033[0m");
                }
                else if (s.getMap()[i][j] == Case.Cactus) {
                    out.append("\033[93mC\033[0m");
                }
                else if (s.getMap()[i][j] == Case.Rock) {
                    out.append("\033[90mR\033[0m");
                }
                out.append(" | ");
            }
            out.append("\n");
            out.append(str);
        }
        System.out.println(out);
    }
}