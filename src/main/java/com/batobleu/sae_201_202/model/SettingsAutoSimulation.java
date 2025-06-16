package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.model.algo.PathFinding;

public class SettingsAutoSimulation {
    private int dManhattan;
    private PathFinding algoSheep;
    private PathFinding algoWolf;
    private boolean vision;

    public SettingsAutoSimulation(int dManhattan, PathFinding algoSheep, PathFinding algoWolf, boolean vision) {
        this.dManhattan = dManhattan;
        this.algoSheep = algoSheep;
        this.algoWolf = algoWolf;
        this.vision = vision;
    }

    public int getDManhattan() { return this.dManhattan; }
    public PathFinding getAlgoSheep() { return this.algoSheep; }
    public PathFinding getAlgoWolf() { return this.algoWolf; }
    public boolean getVision() { return this.vision; }
}
