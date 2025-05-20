package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.exception.invalidMap.*;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.entity.Sheep;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.model.tile.TileNotReachable;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class Simulation {
    private static int[] dx = {0, 1, 0, -1};
    private static int[] dy = {1, 0, -1, 0};

    private int nx, ny;
    private MapTile[][] map;
    private boolean chaseMod;
    private Wolf theWolf;
    private Sheep theSheep;

    public Simulation(int nx, int ny) {
        this.nx = nx;
        this.ny = ny;

        init();
    }

    public void init() {
        this.map = new MapTile[ny][nx];
        this.theSheep = null;
        this.theWolf = null;

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

    public List<Integer> findExitMapTile() {
        for(int y = 0; y < this.ny; y++) {
            for(int x = 0; x < this.nx; x++) {
                if(this.map[y][x] == Exit) {
                    List<Integer> pos = new ArrayList<>();
                    pos.add(x);
                    pos.add(y);

                    return pos;
                }
            }
        }

        return null;
    }

    private int countReachableTile() {
        int count = 0;

        for(int y = 0; y < this.ny; y++) {
            for(int x = 0; x < this.nx; x++) {
                if(! (this.map[y][x] instanceof TileNotReachable)) {
                    count++;
                }
            }
        }

        return count;
    }

    private int countWithDFSReachableTile(int x, int y, Set<Pair<Integer, Integer>> visited) {
        visited.add(Pair.with(x, y));

        int count = 1;

        for(int d = 0; d < 4; d++) {
            int _x = x + dx[d], _y = y + dy[d];

            if(_x < 0 || _x >= this.nx || _y < 0 || _y >= this.ny || this.map[_y][_x] instanceof TileNotReachable) {
                continue;
            }

            if(!visited.contains(Pair.with(_x, _y))) {
                count += countWithDFSReachableTile(_x, _y, visited);
            }
        }

        return count;
    }

    public void isValidMap() throws InvalidMapException {
        List<Integer> exitPos = this.findExitMapTile();

        if(exitPos == null) {
            throw new NoExitException();
        }
        if(this.theWolf == null) {
            throw new NoWolfException();
        }
        if(this.theSheep == null) {
            throw new NoSheepException();
        }

        int crt = this.countReachableTile();
        int countWithDFS = this.countWithDFSReachableTile(exitPos.get(0), exitPos.get(1), new HashSet<>());

        System.out.println(crt + " " + countWithDFS);

        if(crt != countWithDFS) {
            throw new UnconnectedGraphException();
        }
    }
}
