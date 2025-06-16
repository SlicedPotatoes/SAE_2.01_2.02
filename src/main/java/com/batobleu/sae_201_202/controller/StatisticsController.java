package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.model.SettingsAutoSimulation;
import com.batobleu.sae_201_202.model.Statistics;
import com.batobleu.sae_201_202.model.algo.PathFinding;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.view.Popup.PopupFileChooser;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.batobleu.sae_201_202.controller.MainController.STRING_ALGORITHM_HASHMAP;

public class StatisticsController extends Application {
    private Stage stage;
    private HBox root;

    private List<CheckBox> cbWolfAlgorithms;
    private List<CheckBox> cbSheepAlgorithms;
    private ListView<String> fileListView;
    private Spinner<Integer> nbIteration;
    private CheckBox cb;
    private Spinner<Integer> distanceManhattan;

    private ProgressBar progressCompute;

    private List<Statistics> statistics;

    private Button buttonConfirm;

    @Override
    public void start(Stage stage) {
        MainController.setupConstant();

        this.stage = stage;
        this.root = new HBox();
        this.root.setSpacing(30);
        this.root.setPadding(new Insets(10));

        this.cbWolfAlgorithms = new ArrayList<>();
        this.cbSheepAlgorithms = new ArrayList<>();

        Scene scene = new Scene(root, 1280, 720);
        this.stage.setTitle("Mange moi si tu peux ! | Statistiques");
        this.stage.setScene(scene);

        // Algorithme du loup
        VBox containerAlgoWolf = new VBox();
        containerAlgoWolf.setSpacing(5);
        Label titleAlgoWolf = new Label("Algorithme\ndu loup");
        titleAlgoWolf.setTextAlignment(TextAlignment.CENTER);
        titleAlgoWolf.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        containerAlgoWolf.getChildren().add(titleAlgoWolf);

        for (String s : MainController.WOLF_ALGORITHM) {
            CheckBox cb = new CheckBox(s);
            cb.setIndeterminate(false);
            containerAlgoWolf.getChildren().add(cb);

            this.cbWolfAlgorithms.add(cb);
        }

        // Algorithme du mouton
        VBox containerAlgoSheep = new VBox();
        containerAlgoSheep.setSpacing(5);
        Label titleAlgoSheep = new Label("Algorithme\ndu mouton");
        titleAlgoSheep.setTextAlignment(TextAlignment.CENTER);
        titleAlgoSheep.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        containerAlgoSheep.getChildren().add(titleAlgoSheep);

        for(String s : MainController.SHEEP_ALGORITHM) {
            CheckBox cb = new CheckBox(s);
            cb.setIndeterminate(false);
            containerAlgoSheep.getChildren().add(cb);
            this.cbSheepAlgorithms.add(cb);
        }

        // Scénario
        VBox containerScenario = new VBox();
        containerScenario.setSpacing(5);
        Label titleScenario = new Label("Scénario");
        titleScenario.setTextAlignment(TextAlignment.CENTER);
        titleScenario.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        this.fileListView = new ListView<>();
        this.fileListView.setPlaceholder(new Label("Aucun fichier"));

        HBox containerButton = getHBox();
        containerScenario.getChildren().addAll(titleScenario, containerButton, this.fileListView);

        // Configuration
        GridPane containerConfig = new GridPane();
        containerConfig.setHgap(5);
        containerConfig.setVgap(5);

        Label titleConfiguration = new Label("Configuration");
        titleConfiguration.setTextAlignment(TextAlignment.CENTER);
        titleConfiguration.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        this.nbIteration = new Spinner<>(10, 1000000, 100);
        this.nbIteration.setEditable(true);
        this.cb = new CheckBox("Vision à travers les rochers");
        this.cb.setIndeterminate(false);
        this.distanceManhattan = new Spinner<>(0, 999, 5);
        this.distanceManhattan.setEditable(true);

        this.buttonConfirm = new Button("Valider");

        this.buttonConfirm.setOnAction(e -> {
            this.startSimulation();
        });

        this.progressCompute = new ProgressBar(0);

        containerConfig.add(titleConfiguration, 0, 0);
        containerConfig.add(new Label("Nombre d'itération par tuple\n(AlgoL, AlgoM, Scenario)"), 0, 1);
        containerConfig.add(this.nbIteration, 1, 1);
        containerConfig.add(this.cb, 0, 2);
        containerConfig.add(new Label("Distance de Manhattan: "), 0, 3);
        containerConfig.add(this.distanceManhattan, 1, 3);
        containerConfig.add(this.buttonConfirm, 0, 4);

        VBox containerProgress = new VBox();
        containerProgress.setSpacing(5);
        containerProgress.getChildren().addAll(this.progressCompute);

        this.root.getChildren().addAll(containerAlgoWolf, containerAlgoSheep, containerScenario, containerConfig, containerProgress);

        stage.show();
    }

    private HBox getHBox() {
        Button addButton = new Button("Ajouter un fichier");
        addButton.setOnAction(e -> {
            String pathString = new PopupFileChooser().getPath("Ouvrir", this.stage, PopupFileChooser.Type.Import);

            if(pathString != null) {
                fileListView.getItems().add(pathString);
            }
        });

        Button removeButton = new Button("Supprimer le fichier sélectionné");
        removeButton.setOnAction(e -> {
            String selectedFile = fileListView.getSelectionModel().getSelectedItem();
            if(selectedFile != null) {
                fileListView.getItems().remove(selectedFile);
            }
        });

        return new HBox(10, addButton, removeButton);
    }

    private void startSimulation() {
        statistics = new ArrayList<>();
        List<String> algosWolf = new ArrayList<>();
        List<String> algosSheep = new ArrayList<>();

        for(CheckBox cb : cbWolfAlgorithms) {
            if(cb.isSelected()){
                algosWolf.add(cb.getText());
            }
        }
        for(CheckBox cb : cbSheepAlgorithms) {
            if(cb.isSelected()){
                algosSheep.add(cb.getText());
            }
        }

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws Exception {
                int totalTask = algosWolf.size() * algosSheep.size() * fileListView.getItems().size() * nbIteration.getValue();
                int curr = 0;
                buttonConfirm.setDisable(true);


                updateProgress(0, totalTask);
                for (String string : algosWolf) {
                    PathFinding aw = STRING_ALGORITHM_HASHMAP.get(string);
                    for (String sheep : algosSheep) {
                        PathFinding as = STRING_ALGORITHM_HASHMAP.get(sheep);
                        for (int k = 0; k < fileListView.getItems().size(); k++) {
                            try {
                                SettingsAutoSimulation settings = new SettingsAutoSimulation(distanceManhattan.getValue(), as, aw, cb.isSelected());
                                Statistics s = new Statistics(fileListView.getItems().get(k), nbIteration.getValue(), settings);

                                for (int l = 0; l < nbIteration.getValue(); l++) {
                                    s.simulate();
                                    updateProgress(++curr, totalTask);
                                }

                                System.out.println(sheep);
                                System.out.println(fileListView.getItems());
                                System.out.println("AvgNbTurn: " + s.getAvgNbTurn());
                                System.out.println("Winrate: " + s.getWinRate() + "%");
                                System.out.println("CycleRate: " + s.getCycleRate() + "%");
                                System.out.println("AvgNbExpByGame: " + s.getAvgExplorationByGame());
                                System.out.println("AvgTimeByGame: " + s.getAvgTimeByGame() + "ns");
                                HashMap<MapTile, Double> avgHerbEat = s.getAvgHerbEat();
                                for (MapTile mt : avgHerbEat.keySet()) {
                                    System.out.println(mt.getLabel() + ": " + avgHerbEat.get(mt));
                                }
                                System.out.println("-----------------");

                                statistics.add(s);
                            } catch (Exception e) {
                                System.out.println("Erreur lors de la simulation : " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                }

                buttonConfirm.setDisable(false);
                return null;
            }
        };

        this.progressCompute.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
}
