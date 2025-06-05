package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.*;
import javafx.util.Pair;

import java.util.*;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class BfsMatthis extends PathFinding{
    private MapTile[][] map;
    private ArrayList<Pair<Integer,Integer>> chemin;
    private Simulation sim;

    public BfsMatthis() {
    }

    public ArrayList<Pair<Integer, Integer>> voisins(int y, int x) {
        ArrayList<Pair<Integer, Integer>> v = new ArrayList<>();
        if (y > 0 && map[y - 1][x] != ROCK) {
            v.add(new Pair<>(y - 1, x));
        }
        if (x > 0 && map[y][x - 1] != ROCK) {
            v.add(new Pair<>(y, x - 1));
        }
        if (y < map.length - 1 && map[y + 1][x] != ROCK) {
            v.add(new Pair<>(y + 1, x));
        }
        if (x < map[0].length - 1 && map[y][x + 1] != ROCK) {
            v.add(new Pair<>(y, x + 1));
        }
        return v;
    }

    public ArrayList<Pair<Integer, Integer>> getChemin() {
        //initialisation
        Pair<Integer, Integer> start = new Pair<>(this.sim.getSheep().getY(), this.sim.getSheep().getX());
        ArrayList<Pair<Integer, Integer>> visited = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> queue = new ArrayList<>();
        HashMap<Pair<Integer, Integer>,Pair<Integer, Integer>> precedent = new HashMap<>();
        precedent.put(start, null);
        queue.add(start);
        visited.add(start);
        List<Integer> temp = this.sim.findExitMapTile();
        Pair<Integer, Integer> exit = new Pair<>(temp.get(1), temp.get(0));

        //Parcours
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> curr = queue.getFirst();
            if (curr == exit) {
                break;
            }
            ArrayList<Pair<Integer, Integer>> v = voisins(curr.getKey(), curr.getValue());
            for (Pair<Integer, Integer> p1 : v) {
                if (!visited.contains(p1)) {
                    visited.add(p1);
                    queue.add(p1);
                    precedent.put(p1, curr);
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
                chemin.add(new Pair<>(ancien.getValue() - courant.getValue(), ancien.getKey() - courant.getKey()));
            }
        }

        //Inversion du chemin
        Collections.reverse(chemin);
        return chemin;
    }

    @Override
    public Pair<Integer, Integer> nextMove(Simulation sim) {
        this.sim = sim;
        this.map = sim.getMap();

        if (this.chemin == null || this.chemin.isEmpty()) {
            this.chemin = getChemin();
        }

        Pair<Integer, Integer> a = this.chemin.getFirst();
        this.chemin.remove(a);
        return a;
    }
}
