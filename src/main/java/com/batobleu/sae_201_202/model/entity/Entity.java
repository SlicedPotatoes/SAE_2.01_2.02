package com.batobleu.sae_201_202.model.entity;

import com.batobleu.sae_201_202.exception.IllegalMoveException;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.model.tile.TileNotReachable;

public abstract class Entity {
    protected int x, y, speed;

    public Entity(int _x, int _y, int _speed){
        this.x = _x;
        this.y = _y;
        this.speed = _speed;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getSpeed() {
        return this.speed;
    }

    abstract public void move(int dX, int dY) throws IndexOutOfBoundsException, IllegalMoveException;

    public void move(int dX, int dY, MapTile[][] map) throws IndexOutOfBoundsException, IllegalMoveException {
        int x = this.x + dX;
        int y = this.y + dY;

        if(x < 0 || x >= map[0].length || y < 0 || y >= map.length) {
            throw new IndexOutOfBoundsException("Sortie de la map");
        }

        if(map[y][x] instanceof TileNotReachable) {
            throw new IllegalMoveException("Deplacement sur un rocher");
        }

        this.x = x;
        this.y = y;
    }
}

