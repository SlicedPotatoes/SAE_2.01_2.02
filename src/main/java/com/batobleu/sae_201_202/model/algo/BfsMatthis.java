package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.*;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class BfsMatthis {
    private Simulation sim;
    private MapTile[][] map;

    public BfsMatthis(Simulation sim) {
        this.sim = sim;
        this.map = sim.getMap();
        long start = System.nanoTime();
        System.out.println(getChemin());
        long end = System.nanoTime();
        System.out.println("Temp : " + (end - start) / 1e9);

    }

    public ArrayList<Pair<Integer, Integer>> voisins(int x, int y) {
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

    public int manhattan(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        return Math.abs(a.getValue0() - b.getValue0()) + Math.abs(a.getValue1() - b.getValue1());
    }

    public ArrayList<Pair<Integer, Integer>> getChemin() {
        Pair<Integer, Integer> start = new Pair<>(this.sim.getSheep().getY(), this.sim.getSheep().getX());
        ArrayList<Pair<Integer, Integer>> visited = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> queue = new ArrayList<>();
        HashMap<Pair<Integer, Integer>,Pair<Integer, Integer>> precedent = new HashMap<>();
        precedent.put(start, null);

        queue.add(start);
        visited.add(start);
        List<Integer> temp = this.sim.findExitMapTile();
        Pair<Integer, Integer> exit = new Pair<>(temp.get(1), temp.get(0));
        while (!queue.isEmpty() || !visited.contains(start)) {
            Pair<Integer, Integer> curr = queue.getFirst();
            if (curr == exit) {
                break;
            }
            ArrayList<Pair<Integer, Integer>> v = voisins(curr.getValue1(), curr.getValue0());
            for (Pair<Integer, Integer> p1 : v) {
                if (!visited.contains(p1)) {
                    visited.add(p1);
                    queue.add(p1);
                    precedent.put(p1, curr);
                }
            }
            queue.remove(curr);

        }
        ArrayList<Pair<Integer, Integer>> chemin = new ArrayList<>();
        Pair<Integer, Integer> courant = exit;
        while (courant != null) {
            Pair<Integer, Integer> a = courant;
            courant = precedent.get(courant);
            if (courant != null) {
                chemin.add(new Pair<>(a.getValue0() - courant.getValue0(), a.getValue1() - courant.getValue1()));
            }
        }

        return chemin;
    }
}
