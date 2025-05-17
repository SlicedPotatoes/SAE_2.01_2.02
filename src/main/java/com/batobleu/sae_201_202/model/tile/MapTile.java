package com.batobleu.sae_201_202.model.tile;

public class MapTile {
    private String pathIcon;
    private String label;

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
}
