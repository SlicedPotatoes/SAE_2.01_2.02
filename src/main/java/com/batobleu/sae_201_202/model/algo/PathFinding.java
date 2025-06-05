package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.Simulation;
import javafx.util.Pair;

public abstract class PathFinding {
    public abstract Pair<Integer, Integer> nextMove(Simulation s);
}
