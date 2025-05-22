package com.batobleu.sae_201_202.model.tile;

public class TileEntity extends MapTile {
    public TileEntity(String pathIcon, String label) {
        super(pathIcon, label);
    }

    // Une entité peut seulement être placée sur une TileHerb
    @Override
    public boolean isValidPosition(int x, int y, int nx, int ny, MapTile mt) {
        return mt instanceof TileHerb;
    }
}
