package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.Simulation;
import javafx.util.Pair;

import java.util.List;

public class NoMove extends PathFinding {
    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        return List.of(new Pair<>(0, 0));
    }
}
