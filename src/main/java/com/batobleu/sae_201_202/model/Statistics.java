package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.model.tile.TileEntity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.batobleu.sae_201_202.controller.MainController.CHARACTER_MAP_TILE_HASH_MAP;

public class Statistics {
    private final String scenario;
    private final int nbIteration;
    private final SettingsAutoSimulation settings;

    private List<Integer> listExplorations;
    private List<Long> listTimes;
    private int win;
    private List<HashMap<MapTile, Integer>> listHerbsEat;
    private int nbTurn;

    public Statistics(String scenario, int nbIteration, SettingsAutoSimulation settings) {
        this.scenario = scenario;
        this.nbIteration = nbIteration;
        this.settings = settings;
    }

    private Simulation setupSimulation(List<String> lineFiles) {
        int nx = lineFiles.getFirst().length();
        int ny = lineFiles.size();

        Simulation s = new Simulation(nx, ny, null);

        for(int y = 0; y < ny; y++) {
            for(int x = 0; x < nx; x++) {
                MapTile mt = CHARACTER_MAP_TILE_HASH_MAP.get(lineFiles.get(y).charAt(x));

                if(mt instanceof TileEntity) {
                    s.setEntity(mt, x, y);
                }
                else {
                    s.getMap()[y][x] = mt;
                }
            }
        }

        return s;
    }

    public void simulate() {
        try {
            List<String> lineFiles = Files.readAllLines(Path.of(this.scenario));
            this.listExplorations = new ArrayList<>();
            this.listTimes = new ArrayList<>();
            this.listHerbsEat = new ArrayList<>();
            this.win = 0;
            this.nbTurn = 0;

            for(int i = 0; i < this.nbIteration; i++) {
                Simulation s = setupSimulation(lineFiles);
                s.autoSimulation(
                        this.settings.getDManhattan(),
                        this.settings.getAlgoSheep(),
                        this.settings.getAlgoWolf(),
                        this.settings.getVision()
                );
                s.setLast();

                this.listExplorations.add(s.getSumExplorations());
                this.listTimes.add(s.getSumTimes());
                this.listHerbsEat.add(s.getCounts());
                this.nbTurn += s.getCurrRound();

                if(s.getSheep().getIsSafe()) {
                    this.win++;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Erreur lors de la simulation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double getWinRate() {
        return this.win / (double)this.nbIteration;
    }

    public HashMap<MapTile, Double> getAvgHerbEat() {
        HashMap<MapTile, Double> result = new HashMap<>();

        for(HashMap<MapTile, Integer> mp : this.listHerbsEat) {
            for(MapTile mt : mp.keySet()) {
                result.put(mt, result.getOrDefault(mt, 0.0) + mp.get(mt));
            }
        }

        result.replaceAll((mt, v) -> v / this.nbIteration);

        return result;
    }

    public double getAvgNbTurn() {
        return this.nbTurn / (double)this.nbIteration;
    }

    public double getAvgExplorationByGame() {
        int sum = 0;

        for (int value : this.listExplorations) {
            sum += value;
        }

        return sum / (double)this.nbIteration;
    }

    public double getAvgTimeByGame() {
        long sum = 0;

        for(long value : this.listTimes) {
            sum += value;
        }

        return sum / (double)this.nbIteration;
    }
}
