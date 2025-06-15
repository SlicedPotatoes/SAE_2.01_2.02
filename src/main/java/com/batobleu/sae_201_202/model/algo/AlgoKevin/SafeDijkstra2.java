package com.batobleu.sae_201_202.model.algo.AlgoKevin;

import com.batobleu.sae_201_202.model.Simulation;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SafeDijkstra2 extends DijkstraKevin {
    protected double[][] dangersValues;
    protected boolean[][] visited;

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        super.s = s;

        this.dangersValues = new double[s.getNy()][s.getNx()];
        this.visited = new boolean[s.getNy()][s.getNx()];

        int[][] cost = new int[s.getNy()][s.getNx()];

        for(int y = 0; y < s.getNy(); y++) {
            for(int x = 0; x < s.getNx(); x++) {
                this.visited[y][x] = false;
                cost[y][x] = s.getNx()*s.getNy();
            }
        }

        int x = s.getWolf().getX(), y = s.getWolf().getY();

        Queue<BFSKevinQueueData> q = new LinkedList<>();
        q.add(new BFSKevinQueueData(new Pair<>(x, y), 0));
        cost[y][x] = 0;
        dangersValues[y][x] = s.getNx()*s.getNy();

        while(!q.isEmpty()) {
            BFSKevinQueueData curr = q.poll();
            x = curr.getPos().getKey();
            y = curr.getPos().getValue();
            int currTurn = cost[y][x] + 1;

            List<Pair<Integer, Integer>> neighbors = getNeighbors(x, y, true);

            for(Pair<Integer, Integer> neighbor : neighbors) {
                int _x = neighbor.getKey();
                int _y = neighbor.getValue();

                if(cost[_y][_x] <= currTurn) {
                    continue;
                }

                cost[_y][_x] = currTurn;
                dangersValues[_y][_x] = s.getNy() * s.getNx() * Math.pow(Math.E, -currTurn);
                q.add(new BFSKevinQueueData(new Pair<>(_x, _y), 0));
            }
        }

        System.out.println("Cost: ");
        for(y = 0; y < s.getNy(); y++) {
            for(x = 0; x < s.getNx(); x++) {
                System.out.print(cost[y][x] + "\t");
            }
            System.out.println();
        }
        System.out.println("DangersValues: ");
        for(y = 0; y < s.getNy(); y++) {
            for(x = 0; x < s.getNx(); x++) {
                System.out.print(Math.round(dangersValues[y][x] * 100) / (double)100 + "\t");
            }
            System.out.println();
        }

        return super.nextMove(s);
    }

    @Override
    protected void addElementToQueue(Queue<BFSKevinQueueData> q, BFSKevinQueueData element) {
        int x = element.getPos().getKey(), y = element.getPos().getValue();

        this.visited[y][x] = true;
        element.setCurrCost(this.dangersValues[y][x]);
        q.add(element);
    }

    @Override
    protected boolean needToBeAddedToQueue(int x, int y) {
        return !this.visited[y][x];
    }
}
