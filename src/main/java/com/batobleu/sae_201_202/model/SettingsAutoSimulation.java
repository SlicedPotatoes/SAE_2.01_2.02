package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.model.algo.PathFinding;

public class SettingsAutoSimulation {
    private int dManhattan;
    private PathFinding algoSheep;
    private PathFinding algoWolf;

    public SettingsAutoSimulation(int dManhattan, PathFinding algoSheep, PathFinding algoWolf) {
        this.dManhattan = dManhattan;
        this.algoSheep = algoSheep;
        this.algoWolf = algoWolf;
    }

    public int getDManhattan() { return this.dManhattan; }
    public PathFinding getAlgoSheep() { return this.algoSheep; }
    public PathFinding getAlgoWolf() { return this.algoWolf; }
}
