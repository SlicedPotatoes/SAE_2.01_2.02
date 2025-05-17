package com.batobleu.sae_201_202.model;

public class TileHerb extends MapTile {
    private float speedModifier;

    public TileHerb(String pathIcon, float speedModifier) {
        super(pathIcon);
        this.speedModifier = speedModifier;
    }

    public float getSpeedModifier() {
        return this.speedModifier;
    }
}
