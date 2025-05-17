package com.batobleu.sae_201_202.model;

public class Simulation {
    public static MapTile Rock = new TileNotReachable("/Image/Rocher.png");
    public static MapTile Cactus = new TileHerb("/Image/Cactus.png", 0.5f);
    public static MapTile Grass = new TileHerb("/Image/Herbe.png", 1f);
    public static MapTile Poppy = new TileHerb("/Image/Marguerite.png", 2f);
    public static MapTile Exit = new TileExit("/Image/Exit.png");

    private int nx, ny;
    private MapTile[][] map;
    private boolean chaseMod;
    private Wolf theWolf;
    private Sheep theSheep;

    public Simulation(int nx, int ny) {
        this.nx = nx;
        this.ny = ny;

        this.map = new MapTile[ny][nx];

        for (int row = 0; row < ny; row++) {
            for (int col = 0; col < nx; col++) {
                if (row == 0 || col == 0 || row == ny - 1 || col == nx - 1) {
                    this.map[row][col] = Rock;
                } else {
                    this.map[row][col] = Grass;
                }
            }
        }
    }

    public MapTile[][] getMap() {
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
