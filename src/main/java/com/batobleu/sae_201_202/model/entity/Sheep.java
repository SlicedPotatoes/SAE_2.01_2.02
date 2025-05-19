package com.batobleu.sae_201_202.model.entity;

import com.batobleu.sae_201_202.exception.IllegalMoveException;
import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.TileHerb;

public class Sheep extends Entity {
    private Simulation theSimulation;
    private float speedModifier;
    private boolean isEaten;
    private boolean isSafe;

    public Sheep(int x, int y, int speed, Simulation s) {
        super(x, y, speed);
        this.theSimulation = s;
        this.speedModifier = 1;
        this.isEaten = false;
        this.isSafe = false;
    }

    @Override
    public int getSpeed() {
        return (int) (super.getSpeed() * speedModifier);
    }

    @Override
    public void move(int dX, int dY) throws IndexOutOfBoundsException, IllegalMoveException {
        super.move(dX, dY, this.theSimulation.getMap());
        this.eat();
    }

    public boolean getIsEaten() {
        return this.isEaten;
    }

    public boolean getIsSafe() {
        return this.isSafe;
    }

    public void eaten() {
        this.isEaten = true;
    }

    public void safe() {
        this.isSafe = true;
    }

    private void eat() {
        if(this.theSimulation.getMap()[super.y][super.x] instanceof TileHerb t) {
            this.speedModifier = t.getSpeedModifier();
        }
    }
}