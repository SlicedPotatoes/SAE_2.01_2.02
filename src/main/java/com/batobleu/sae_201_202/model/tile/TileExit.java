package com.batobleu.sae_201_202.model.tile;

public class TileExit extends MapTile {
    public TileExit(String pathIcon, String label) {
        super(pathIcon, label);
    }

    @Override
    public boolean isValidPosition(int x, int y, int nx, int ny, MapTile mt) {
        return x == 0 || y == 0 || x == nx - 1 || y == ny - 1;
    }
}
