package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.entity.Sheep;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.model.tile.MapTile;

import static com.batobleu.sae_201_202.controller.MainController.Herb;
import static com.batobleu.sae_201_202.controller.MainController.Rock;

public class Simulation {
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
                    this.map[row][col] = Herb;
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

    // poor entity 😢
    public void killEntity(Entity entity) {
        if(entity == this.theWolf) {
            this.theWolf = null;
        }
        else {
            this.theSheep = null;
        }
    }

    public void setEntity(MapTile entity, int x, int y) {
        if(entity == MainController.Wolf) {
            this.theWolf = new Wolf(x, y, 3, this);
        }
        else {
            this.theSheep = new Sheep(x, y, 2, this);
        }
    }

    public int getNx() {
        return this.nx;
    }

    public int getNy() {
        return this.ny;
    }

}
