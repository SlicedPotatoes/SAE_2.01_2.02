package com.batobleu.sae_201_202.model.algo.AlgoKevin;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.TileHerb;
import javafx.util.Pair;

import java.util.List;
import java.util.Queue;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class AStarKevin1 extends DijkstraKevin {
    protected double weight;

    public AStarKevin1() {}

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        this.weight = DEFAULT_SPEED_WOLF;

        if(s.getCurrEntityTurn() == SHEEP) {
            for(int y = 0; y < s.getNy(); y++) {
                for(int x = 0; x < s.getNx(); x++) {
                    if(s.getMap()[y][x] instanceof TileHerb) {
                        this.weight = Math.max(this.weight, DEFAULT_SPEED_SHEEP * ((TileHerb) s.getMap()[y][x]).getSpeedModifier());
                    }
                }
            }
        }

        return super.nextMove(s);
    }

    @Override
    protected void addElementToQueue(Queue<BFSKevinQueueData> q, BFSKevinQueueData element) {
        int x = element.getPos().getKey(), y = element.getPos().getValue();
        double heuristic = (Math.abs(x - this.target.getKey()) + Math.abs(y - this.target.getValue())) / this.weight;

        //System.out.println(x + " " + y + " " + super.mapCost[y][x] + " " + heuristic + " " + (super.mapCost[y][x] + heuristic));

        element.setCurrCost(super.mapCost[y][x] + heuristic);
        q.add(element);
    }
}
