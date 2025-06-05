package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.model.tile.TileNotReachable;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.batobleu.sae_201_202.controller.MainController.EXIT;
import static com.batobleu.sae_201_202.controller.MainController.SHEEP;
import static com.batobleu.sae_201_202.model.Simulation.*;

public class Random extends PathFinding {

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        List<Pair<Integer, Integer>> possibleMoves = new ArrayList<>();

        Entity e = s.getCurrEntityTurn() == SHEEP ? s.getSheep() : s.getWolf();

        for (int d = 0; d < 4; d++) {
            int x = e.getX() + dx[d];
            int y = e.getY() + dy[d];

            if(x < 0 || x >= s.getNx() || y < 0 || y >= s.getNy() || s.getMap()[y][x] instanceof TileNotReachable) {
                continue;
            }
            if(e instanceof Wolf && s.getMap()[y][x] == EXIT) {
                continue;
            }

            possibleMoves.add(new Pair<>(dx[d], dy[d]));
        }

        List<Pair<Integer, Integer>> result = new ArrayList<>();
        result.add(possibleMoves.get(new java.util.Random().nextInt(possibleMoves.size())));

        return result;
    }
}
