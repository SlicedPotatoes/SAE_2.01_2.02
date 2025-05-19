package com.batobleu.sae_201_202.model.tile;

public class TileNotReachable extends MapTile {
    public TileNotReachable(String pathIcon, String label) {
        super(pathIcon, label);
    }

    @Override
    public boolean isValidPosition(int x, int y, int nx, int ny, MapTile mt) {
        return true;
    }
}
