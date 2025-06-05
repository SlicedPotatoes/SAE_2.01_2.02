package com.batobleu.sae_201_202.model.algo.BFSKevin;

import javafx.util.Pair;

public class BFSKevinQueueData {
    private final Pair<Integer, Integer> pos;
    private final int currTurn;
    private final int nbDep;

    public BFSKevinQueueData(Pair<Integer, Integer> pos, int currTurn, int nbDep) {
        this.pos = pos;
        this.currTurn = currTurn;
        this.nbDep = nbDep;
    }

    public Pair<Integer, Integer> getPos() {
        return this.pos;
    }

    public int getCurrTurn() {
        return this.currTurn;
    }

    public int getNbDep() {
        return this.nbDep;
    }
}
