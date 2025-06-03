package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.MapTile;
import javafx.util.Pair;

public abstract class PathFinding {
    public abstract Pair<Integer, Integer> nextMove(MapTile[][] map, Entity e, Entity other);
}
