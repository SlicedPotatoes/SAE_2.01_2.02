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

    // Une herbe peut être placée partout sauf sur les bords
    @Override
    public boolean isValidPosition(int x, int y, int nx, int ny, MapTile mt) {
        return x > 0 && x < nx-1 && y > 0 && y < ny-1;
    }
}
