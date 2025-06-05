package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.Simulation;
import javafx.util.Pair;

import java.util.List;

public abstract class PathFinding {
    public abstract List<Pair<Integer, Integer>> nextMove(Simulation s);
}
