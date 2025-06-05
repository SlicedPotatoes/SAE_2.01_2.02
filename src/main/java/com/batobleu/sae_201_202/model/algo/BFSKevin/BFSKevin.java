package com.batobleu.sae_201_202.model.algo.BFSKevin;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.algo.PathFinding;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.TileExit;
import com.batobleu.sae_201_202.model.tile.TileHerb;
import com.batobleu.sae_201_202.model.tile.TileNotReachable;
import javafx.util.Pair;

import java.util.*;

import static com.batobleu.sae_201_202.controller.MainController.*;
import static com.batobleu.sae_201_202.model.Simulation.*;

public class BFSKevin extends PathFinding {
    private Simulation s;
    // Matrice des coûts pour arriver à une case X, Y depuis le départ
    private int[][] mapCost;
    // Matrice des prédécesseurs pour arriver à une case X, Y avec le coût le plus faible
    private int[][][] mapPrev;

    private boolean isSheep;
    private Pair<Integer, Integer> target;
    private HashMap<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, List<Pair<Integer, Integer>>> intermediateMovement;


    public BFSKevin() {}

    // Initialise la matrice des coûts et des prédécesseurs
    private void init(int nx, int ny) {
        this.mapCost = new int[ny][nx];
        this.mapPrev = new int[ny][nx][2];
        this.intermediateMovement = new HashMap<>();

        for(int y = 0; y < ny; y++) {
            for(int x = 0; x < nx; x++) {
                mapCost[y][x] = nx + ny + 1;
                mapPrev[y][x][0] = -1;
                mapPrev[y][x][1] = -1;
            }
        }
    }

    // Récupère le nombre de déplacements dans le tour actuel
    public int getSpeed(int x, int y) {
        if (! (this.s.getMap()[y][x] instanceof TileHerb)) {
            return 0;
        }
        return this.isSheep ? (int) (DEFAULT_SPEED_SHEEP * ((TileHerb) this.s.getMap()[y][x]).getSpeedModifier()) : DEFAULT_SPEED_WOLF;
    }

    // Récupère les voisins, pour prendre en compte les plantes, on part du principe qu'une
    // case est adjacente si elle est atteignable à la fin de tous les déplacements d'un tour.
    // Cette méthode construit également le chemin intermédiaire pour atteindre les cases entre les tours
    // (Chemin pour aller de position_depart_tourX jusqu'à position_depart_tourX+1)
    public void neighbor(int x, int y, Set<Pair<Integer, Integer>> result, int nbDep, Pair<Integer, Integer> start, List<Pair<Integer, Integer>> path) {
        // Condition d'arrêt, quand c'est une case de fin de tour (nbDep <= 0) ou que la cible est atteinte.
        if (nbDep <= 0 || (x == this.target.getKey() && y == this.target.getValue())) {
            Pair<Integer, Integer> end = new Pair<>(x, y);

            // Ajouter le chemin intermédiaire entre la case de départ et celle de fin du tour.
            this.intermediateMovement.put(new Pair<>(start, end), new ArrayList<>(path));
            // Ajouter la case de fin de tour au résultat
            result.add(end);
            return;
        }

        // Parcours des déplacements possibles
        for(int d = 0; d < 4; d++) {
            int _x = x + dx[d];
            int _y = y + dy[d];

            // Le déplacement fait sortir l'entité de la grille
            if(_x < 0 || _x >= this.s.getNx() || _y < 0 || _y >= this.s.getNy()) {
                continue;
            }
            // Le déplacement mène sur une case inaccessible
            if (this.s.getMap()[_y][_x] instanceof TileNotReachable) {
                continue;
            }
            // Cas du loup qui ne peut pas aller sur la case de sortie
            if (!this.isSheep && this.s.getMap()[_y][_x] instanceof TileExit) {
                continue;
            }

            path.add(new Pair<>(dx[d], dy[d]));
            this.neighbor(_x, _y, result, nbDep - 1, start, path);
            path.removeLast();
        }
    }

    // Parcours en largeur
    public void compute(Pair<Integer, Integer> start) {
        // Initialisation
        Queue<BFSKevinQueueData> q = new LinkedList<>();
        q.add(new BFSKevinQueueData(start, 0, this.getSpeed(start.getKey(), start.getValue())));
        this.mapCost[start.getValue()][start.getKey()] = 0;

        while (!q.isEmpty()) {
            BFSKevinQueueData curr = q.poll();
            Pair<Integer, Integer> pos = curr.getPos();

            // Si la case actuelle est la cible, on arrête d'itérer car c'est le chemin le plus court.
            if(pos.getKey().equals(this.target.getKey()) && pos.getValue().equals(this.target.getValue())) {
                break;
            }

            // Utilisation d'un Set pour stocker les voisins, cela permet d'éviter les doublons
            Set<Pair<Integer, Integer>> moves = new HashSet<>();
            // Stocke le chemin intermédiaire
            List<Pair<Integer, Integer>> path = new ArrayList<>();
            this.neighbor(pos.getKey(), pos.getValue(), moves, curr.getNbDep(), pos, path);

            // Parcours des voisins
            for (Pair<Integer, Integer> move : moves) {
                int _x = move.getKey();
                int _y = move.getValue();

                // Si la case est atteignable plus rapidement, on l'ignore
                if(this.mapCost[_y][_x] <= curr.getCurrTurn() ){
                    continue;
                }

                // On met à jour la map des prédécesseurs et des coûts
                this.mapCost[_y][_x] = curr.getCurrTurn() + 1;
                this.mapPrev[_y][_x][0] = pos.getKey();
                this.mapPrev[_y][_x][1] = pos.getValue();

                // On ajoute le voisin à la file
                q.add(new BFSKevinQueueData(new Pair<>(_x, _y), curr.getCurrTurn()+1, this.getSpeed(_x, _y)));
            }
        }
    }

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        this.s = s;
        this.init(s.getNx(), s.getNy());
        this.isSheep = s.getCurrEntityTurn() == SHEEP;

        // Entité courante
        Entity e = this.isSheep ? s.getSheep() : s.getWolf();

        // Déterminer la cible
        if(s.getCurrEntityTurn() == SHEEP) {
            List<Integer> l = s.findExitMapTile();
            this.target = new Pair<>(l.get(0), l.get(1));
        }
        else {
            this.target = new Pair<>(s.getSheep().getX(), s.getSheep().getY());
        }

        this.compute(new Pair<>(e.getX(), e.getY()));

        // Debug
        for(int y = 0; y < this.s.getNy(); y++) {
            for(int x = 0; x < this.s.getNx(); x++) {
                System.out.print((this.mapCost[y][x] == s.getNx()+s.getNy()+1 ? "#" : this.mapCost[y][x]) + "\t");
            }
            System.out.println();
        }

        // Construction du chemin
        Pair<Integer, Integer> curr = new Pair<>(this.target.getKey(), this.target.getValue());
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        while(curr.getKey() != e.getX() || curr.getValue() != e.getY()) {
            path.add(curr);
            System.out.println("Path: " + curr);

            int[] c = this.mapPrev[curr.getValue()][curr.getKey()];
            curr = new Pair<>(c[0], c[1]);
        }

        System.out.println("---------------");


        return intermediateMovement.get(new Pair<>( curr, path.getLast() ));
    }
}
