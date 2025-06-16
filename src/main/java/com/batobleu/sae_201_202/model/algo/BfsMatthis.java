package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.Simulation;
import javafx.util.Pair;

import java.util.*;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class BfsMatthis extends PathFinding{
    private ArrayList<Pair<Integer,Integer>> chemin;
    private Boolean isSheep;

    public BfsMatthis() {
    }

    public ArrayList<Pair<Integer, Integer>> getChemin() {
        //initialisation
        Pair<Integer, Integer> start;
        Pair<Integer, Integer> exit;
        if (isSheep){
            List<Integer> temp = super.s.findExitMapTile();
            exit = new Pair<>(temp.get(0), temp.get(1));
            start = new Pair<>(super.s.getSheep().getX(), super.s.getSheep().getY());
        } else{
            exit = new Pair<>(super.s.getSheep().getX(), super.s.getSheep().getY());
            start = new Pair<>(super.s.getWolf().getX(), super.s.getWolf().getY());
        }
        ArrayList<Pair<Integer, Integer>> visited = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> queue = new ArrayList<>();
        HashMap<Pair<Integer, Integer>,Pair<Integer, Integer>> precedent = new HashMap<>();
        precedent.put(start, null);
        queue.add(start);
        visited.add(start);

        //Parcours
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> curr = queue.getFirst();
            if (curr == exit) {
                break;
            }
            List<Pair<Integer, Integer>> v = super.getNeighbors(curr.getKey(), curr.getValue(), true);
            for (Pair<Integer, Integer> p1 : v) {
                if (!visited.contains(p1)) {
                    visited.add(p1);
                    queue.add(p1);
                    precedent.put(p1, curr);

                    super.countTileExplored++;
                }
            }
            queue.remove(curr);
        }

        //Recherche du chemin
        ArrayList<Pair<Integer, Integer>> chemin = new ArrayList<>();
        Pair<Integer, Integer> courant = exit;
        while (courant != null) {
            Pair<Integer, Integer> ancien = courant;
            courant = precedent.get(courant);
            if (courant != null) {
                chemin.add(new Pair<>(ancien.getKey() - courant.getKey(), ancien.getValue() - courant.getValue()));
            }
        }

        //Inversion du chemin
        Collections.reverse(chemin);
        return chemin;
    }

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation sim) {
        super.countTileExplored = 0;
        super.s = sim;
        this.isSheep = sim.getCurrEntityTurn() == SHEEP;

        long startTimes = System.nanoTime();
        this.chemin = getChemin();
        long endTimes = System.nanoTime();
        super.times = endTimes - startTimes;

        return this.chemin;
    }
}
