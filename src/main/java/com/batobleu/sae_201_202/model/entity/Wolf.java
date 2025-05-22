package com.batobleu.sae_201_202.model.entity;

import com.batobleu.sae_201_202.model.exception.IllegalMoveException;
import com.batobleu.sae_201_202.model.Simulation;

public class Wolf extends Entity {
    private Simulation theSimulation;

    public Wolf(int _x, int _y, int _speed, Simulation s) {
        super(_x, _y, _speed);
        this.theSimulation = s;
    }

    @Override
    public void move(int dX, int dY) throws IndexOutOfBoundsException, IllegalMoveException {
         super.move(dX, dY, this.theSimulation.getMap());
    }
}
