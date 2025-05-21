package com.batobleu.sae_201_202.model.entity;

import com.batobleu.sae_201_202.exception.IllegalMoveException;
import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.TileHerb;

import static com.batobleu.sae_201_202.controller.MainController.EXIT;

public class Sheep extends Entity {
    private Simulation theSimulation;
    private float speedModifier;

    public Sheep(int x, int y, int speed, Simulation s) {
        super(x, y, speed);
        this.theSimulation = s;
        this.speedModifier = 1;
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
        Wolf w = this.theSimulation.getWolf();

        return w.x == this.x && w.y == this.y;
    }

    public boolean getIsSafe() {
        return this.theSimulation.getMap()[this.y][this.x] == EXIT;
    }

    private void eat() {
        if(this.theSimulation.getMap()[super.y][super.x] instanceof TileHerb t) {
            this.speedModifier = t.getSpeedModifier();
        }
    }
}