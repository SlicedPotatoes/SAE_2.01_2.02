package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.model.entity.Sheep;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.model.tile.MapTile;

public class HistorySimulation {
    private final Wolf w;
    private final Sheep s;
    private final int ml;
    private final int cr;
    private final MapTile mt;

    public HistorySimulation(Wolf w, Sheep s, int ml, MapTile mt, int cr){
        this.w = new Wolf(w);
        this.s = new Sheep(s);
        this.ml = ml;
        this.mt = mt;
        this.cr = cr;
    }

    public Wolf getWolf() {
        return this.w;
    }

    public Sheep getSheep() {
        return this.s;
    }

    public int getMoveLeft() {
        return this.ml;
    }

    public MapTile getCurrEntityTurn() {
        return this.mt;
    }

    public int getCr() {
        return this.cr;
    }
}
