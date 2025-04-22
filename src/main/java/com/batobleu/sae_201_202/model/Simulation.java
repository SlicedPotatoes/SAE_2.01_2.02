package com.batobleu.sae_201_202.model;

public class Simulation {
    private int nx, ny;
    private Case[][] map;
    private boolean chaseMod;
    private Wolf theWolf;
    private Sheep theSheep;

    public Simulation(int nx, int ny) {
        this.nx = nx;
        this.ny = ny;

        this.map = new Case[ny][nx];

        for (int row = 0; row < ny; row++) {
            for (int col = 0; col < nx; col++) {
                if (row == 0 || col == 0 || row == ny - 1 || col == nx - 1) {
                    this.map[row][col] = Case.Rock;
                } else {
                    this.map[row][col] = Case.Grass;
                }
            }
        }
    }

    public Case[][] getMap() {
        return this.map;
    }

    public Sheep getSheep() {
        return this.theSheep;
    }

    public Wolf getWolf() {
        return this.theWolf;
    }

    public void setSheep(int x, int y) {
        this.theSheep = new Sheep(x, y, 2, this);
    }

    public void setWolf(int x, int y) {
        this.theWolf = new Wolf(x, y, 3, this);
    }

    public int getNx() {
        return this.nx;
    }

    public int getNy() {
        return this.ny;
    }

}
