package com.batobleu.sae_201_202.model.algo;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.tile.TileNotReachable;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.batobleu.sae_201_202.model.Simulation.*;

public abstract class PathFinding {
    protected Simulation s;

    public abstract List<Pair<Integer, Integer>> nextMove(Simulation s);

    // Retourne les voisins accessibles, le paramètre `asCoordinates` permet de choisir
    // si l'on récupère les coordonnées des cases où le déplacement à effectuer.
    protected List<Pair<Integer, Integer>> getNeighbors(int x, int y, boolean asCoordinates) {
        List<Pair<Integer, Integer>> result = new ArrayList<>();

        for(int d = 0; d < 4; d++) {
            int _x = x + dx[d];
            int _y = y + dy[d];

            if(_x < 0 || _x >= this.s.getNx() || _y < 0 || _y >= this.s.getNy()) {
                continue;
            }
            if(this.s.getMap()[_y][_x] instanceof TileNotReachable) {
                continue;
            }

            result.add(asCoordinates ? new Pair<>(_x, _y) : new Pair<>(dx[d], dy[d]));
        }

        return result;
    }
}
