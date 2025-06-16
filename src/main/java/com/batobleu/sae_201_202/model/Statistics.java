package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.model.tile.TileEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.batobleu.sae_201_202.controller.MainController.CHARACTER_MAP_TILE_HASH_MAP;

public class Statistics {
    private final List<String> lineFiles;
    private final int nbIteration;
    private final SettingsAutoSimulation settings;

    private final List<Integer> listExplorations;
    private final List<Long> listTimes;
    private int win;
    private final List<HashMap<MapTile, Integer>> listHerbsEat;
    private int nbTurn;
    private int cycle;

    public Statistics(String scenario, int nbIteration, SettingsAutoSimulation settings) throws IOException {
        this.lineFiles = Files.readAllLines(Path.of(scenario));
        this.nbIteration = nbIteration;
        this.settings = settings;

        this.listExplorations = new ArrayList<>();
        this.listTimes = new ArrayList<>();
        this.listHerbsEat = new ArrayList<>();
        this.win = 0;
        this.nbTurn = 0;
        this.cycle = 0;
    }

    private Simulation setupSimulation() {
        int nx = this.lineFiles.getFirst().length();
        int ny = this.lineFiles.size();

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
            Simulation s = setupSimulation();

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
            else if(!s.getSheep().getIsEaten()) {
                this.cycle++;
            }
        }
        catch (Exception e) {
            System.out.println("Erreur lors de la simulation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double getWinRate() {
        return (this.win / (double)this.nbIteration) * 100;
    }

    public double getCycleRate() {
        return (this.cycle / (double)this.nbIteration) * 100;
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
