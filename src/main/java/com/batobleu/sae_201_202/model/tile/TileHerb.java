package com.batobleu.sae_201_202.model.tile;

public class TileHerb extends MapTile {
    private float speedModifier;

    public TileHerb(String pathIcon, String label, float speedModifier) {
        super(pathIcon, label);
        this.speedModifier = speedModifier;
    }

    public float getSpeedModifier() {
        return this.speedModifier;
    }
}
