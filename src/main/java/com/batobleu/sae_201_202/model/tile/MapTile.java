package com.batobleu.sae_201_202.model.tile;

public abstract class MapTile {
    private final String pathIcon;
    private final String label;

    public MapTile(String pathIcon, String label) {
        this.pathIcon = pathIcon;
        this.label = label;
    }

    public String getPathIcon() {
        return this.pathIcon;
    }

    public String getLabel() {
        return this.label;
    }

    public abstract boolean isValidPosition(int x, int y, int nx, int ny, MapTile mt);
}
