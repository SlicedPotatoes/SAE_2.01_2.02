package com.batobleu.sae_201_202.model.algo.AlgoKevin;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.TileHerb;
import javafx.util.Pair;

import java.util.List;
import java.util.Queue;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class AStarKevin2 extends DijkstraKevin {
    private double weight;

    public AStarKevin2() {}

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        this.weight = DEFAULT_SPEED_WOLF;

        if(s.getCurrEntityTurn() == SHEEP) {
            int countAccessibleTile = 0;
            int sumJump = 0;

            for(int y = 0; y < s.getNy(); y++) {
                for(int x = 0; x < s.getNx(); x++) {
                    if(s.getMap()[y][x] instanceof TileHerb) {
                        countAccessibleTile++;
                        sumJump += (int) (DEFAULT_SPEED_SHEEP * ((TileHerb) s.getMap()[y][x]).getSpeedModifier());
                    }
                }
            }

            if(countAccessibleTile != 0) {
                this.weight = (double) sumJump / countAccessibleTile;
            }
        }

        return super.nextMove(s);
    }

    @Override
    protected void addElementToQueue(Queue<BFSKevinQueueData> q, BFSKevinQueueData element) {
        int x = element.getPos().getKey(), y = element.getPos().getValue();
        double heuristic = (Math.abs(x - this.target.getKey()) + Math.abs(y - this.target.getValue())) / this.weight;

        System.out.println(x + " " + y + " " + super.mapCost[y][x] + " " + heuristic + " " + (super.mapCost[y][x] + heuristic));

        element.setCurrCost(super.mapCost[y][x] + heuristic);
        q.add(element);
    }
}
