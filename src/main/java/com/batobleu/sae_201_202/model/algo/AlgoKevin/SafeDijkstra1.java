package com.batobleu.sae_201_202.model.algo.AlgoKevin;

import com.batobleu.sae_201_202.model.Simulation;
import javafx.util.Pair;

import java.util.List;
import java.util.Queue;

public class SafeDijkstra1 extends DijkstraKevin {
    protected double[][] dangersValues;
    protected boolean[][] visited;

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        this.dangersValues = new double[s.getNy()][s.getNx()];
        this.visited = new boolean[s.getNy()][s.getNx()];

        for(int y = 0; y < s.getNy(); y++) {
            for(int x = 0; x < s.getNx(); x++) {
                int xWolf = s.getWolf().getX(), yWolf = s.getWolf().getY();

                int d = Math.abs(x - xWolf) + Math.abs(y - yWolf);

                //this.dangersValues[y][x] = -d;
                //this.dangersValues[y][x] = ((double) (s.getNx() + s.getNy()) / (1 + d));
                this.dangersValues[y][x] = 100 * Math.pow(Math.E, -d);

                this.visited[y][x] = false;
            }
        }
        return super.nextMove(s);
    }

    @Override
    protected void addElementToQueue(Queue<BFSKevinQueueData> q, BFSKevinQueueData element) {
        int x = element.getPos().getKey(), y = element.getPos().getValue();

        this.visited[y][x] = true;
        element.setCurrCost(super.mapCost[y][x] + this.dangersValues[y][x]);
        q.add(element);
    }

    @Override
    protected boolean needToBeAddedToQueue(int x, int y) {
        return !this.visited[y][x];
    }
}
