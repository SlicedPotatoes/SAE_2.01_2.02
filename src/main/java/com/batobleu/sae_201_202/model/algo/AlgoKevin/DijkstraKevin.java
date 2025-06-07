package com.batobleu.sae_201_202.model.algo.AlgoKevin;

import java.util.*;

public class DijkstraKevin extends BFSKevin {
    public DijkstraKevin() {}

    @Override
    protected Queue<BFSKevinQueueData> getQueue() {
        return new PriorityQueue<>();
    }

    @Override
    protected void addElementToQueue(Queue<BFSKevinQueueData> q, BFSKevinQueueData element) {
        int x = element.getPos().getKey(), y = element.getPos().getValue();

        element.setCurrCost(super.mapCost[y][x]);
        q.add(element);
    }
}
