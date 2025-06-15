package com.batobleu.sae_201_202.model.algo.AlgoKevin;

import javafx.util.Pair;

public class BFSKevinQueueData implements Comparable<BFSKevinQueueData> {
    private final Pair<Integer, Integer> pos;
    private final int nbDep;
    private double currCost;

    public BFSKevinQueueData(Pair<Integer, Integer> pos, int nbDep) {
        this.pos = pos;
        this.nbDep = nbDep;
    }

    public BFSKevinQueueData(Pair<Integer, Integer> pos, int nbDep, int currCost) {
        this.pos = pos;
        this.nbDep = nbDep;
        this.currCost = currCost;
    }

    public Pair<Integer, Integer> getPos() {
        return this.pos;
    }

    public double getCurrCost() {
        return this.currCost;
    }

    public void setCurrCost(double value) {
        this.currCost = value;
    }

    public int getNbDep() {
        return this.nbDep;
    }

    @Override
    public int compareTo(BFSKevinQueueData o) {
        return Double.compare(this.currCost, o.currCost);
    }

    @Override
    public String toString() {
        return "Pos: " + this.pos + " nbDep: " + this.nbDep + " currCost: " + this.currCost;
    }
}
