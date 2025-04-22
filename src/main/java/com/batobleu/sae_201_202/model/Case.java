package com.batobleu.sae_201_202.model;

public enum Case {
    Rock(-1, '#'),
    Cactus(0.5f, 'f'),
    Grass(1, ' '),
    Poppy(2, 'x');

    private final float numericValue;
    private final char icon;

    Case(float numericValue, char icon) {
        this.numericValue = numericValue;
        this.icon = icon;
    }

    public float getNumericValue() {
        return this.numericValue;
    }
    public char getIcon() { return this.icon; }
}