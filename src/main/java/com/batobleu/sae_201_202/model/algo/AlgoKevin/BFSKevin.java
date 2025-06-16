package com.batobleu.sae_201_202.model.algo.AlgoKevin;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.algo.PathFinding;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.TileHerb;
import javafx.util.Pair;

import java.util.*;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class BFSKevin extends PathFinding {
    // Matrice des coûts pour arriver à une case X, Y depuis le départ
    protected int[][] mapCost;
    // Matrice des prédécesseurs pour arriver à une case X, Y avec le coût le plus faible
    protected int[][][] mapPrev;

    protected Pair<Integer, Integer> target;

    private boolean isSheep;
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
    protected int getSpeed(int x, int y) {
        if (! (super.s.getMap()[y][x] instanceof TileHerb)) {
            return 0;
        }
        return this.isSheep ? (int) (DEFAULT_SPEED_SHEEP * ((TileHerb) super.s.getMap()[y][x]).getSpeedModifier()) : DEFAULT_SPEED_WOLF;
    }

    // Récupère les voisins, pour prendre en compte les plantes, on part du principe qu'une
    // case est adjacente si elle est atteignable à la fin de tous les déplacements d'un tour.
    // Cette méthode construit également le chemin intermédiaire pour atteindre les cases entre les tours
    // (Chemin pour aller de position_depart_tourX jusqu'à position_depart_tourX+1).
    protected void neighbor(int x, int y, Set<Pair<Integer, Integer>> result, int nbDep, Pair<Integer, Integer> start, List<Pair<Integer, Integer>> path) {
        // Condition d'arrêt, quand c'est une case de fin de tour (nbDep ≤ 0) ou que la cible est atteinte.
        if (nbDep <= 0 || (x == this.target.getKey() && y == this.target.getValue())) {
            Pair<Integer, Integer> end = new Pair<>(x, y);

            // Ajouter le chemin intermédiaire entre la case de départ et celle de fin du tour.
            this.intermediateMovement.put(new Pair<>(start, end), new ArrayList<>(path));
            // Ajouter la case de fin de tour au résultat
            result.add(end);
            return;
        }

        // Parcours des déplacements possibles
        List<Pair<Integer, Integer>> neighbors = super.getNeighbors(x, y, false);
        for (Pair<Integer, Integer> neighbor : neighbors) {
            int _x = neighbor.getKey();
            int _y = neighbor.getValue();

            path.add(new Pair<>(_x, _y));
            this.neighbor(x + _x, y + _y, result, nbDep - 1, start, path);
            path.removeLast();
        }
    }

    protected Queue<BFSKevinQueueData> getQueue() {
        return new LinkedList<>();
    }

    protected void addElementToQueue(Queue<BFSKevinQueueData> q, BFSKevinQueueData element) {
        q.add(element);
    }

    protected boolean needToBeAddedToQueue(int x, int y) {
        return true;
    }

    // Parcours en largeur
    protected void compute(Pair<Integer, Integer> start) {
        // Initialisation
        Queue<BFSKevinQueueData> q = this.getQueue();
        q.add(new BFSKevinQueueData(start, super.s.getMoveLeft()));
        this.mapCost[start.getValue()][start.getKey()] = 0;

        while (!q.isEmpty()) {
            BFSKevinQueueData curr = q.poll();
            int x = curr.getPos().getKey(), y = curr.getPos().getValue();
            int currTurn = this.mapCost[y][x] + 1;

            // Si la case actuelle est la cible, on arrête d'itérer, car c'est le chemin le plus court.
            if(x == this.target.getKey() && y == this.target.getValue()) {
                break;
            }

            // Utilisation d'un Set pour stocker les voisins, cela permet d'éviter les doublons
            Set<Pair<Integer, Integer>> moves = new HashSet<>();
            // Stocke le chemin intermédiaire
            List<Pair<Integer, Integer>> path = new ArrayList<>();
            this.neighbor(x, y, moves, curr.getNbDep(), curr.getPos(), path);

            // Parcours des voisins
            for (Pair<Integer, Integer> move : moves) {
                int _x = move.getKey();
                int _y = move.getValue();

                // Si la case est atteignable plus rapidement, on l'ignore
                if(this.mapCost[_y][_x] <= currTurn){
                    continue;
                }

                // On met à jour la map des prédécesseurs et des coûts
                this.mapPrev[_y][_x] = new int[]{x, y};
                this.mapCost[_y][_x] = currTurn;

                // On ajoute le voisin à la file
                if (this.needToBeAddedToQueue(_x, _y)) {
                    this.addElementToQueue(q, new BFSKevinQueueData(new Pair<>(_x, _y), this.getSpeed(_x, _y)));
                    super.countTileExplored++;
                }
            }
        }
    }

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        super.countTileExplored = 0;

        super.s = s;
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

        long startTimes = System.nanoTime();
        this.compute(new Pair<>(e.getX(), e.getY()));
        long endTimes = System.nanoTime();
        super.times = endTimes - startTimes;

        startTimes = System.nanoTime();
        // Construction du chemin
        Pair<Integer, Integer> curr = new Pair<>(this.target.getKey(), this.target.getValue());
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        while(curr.getKey() != e.getX() || curr.getValue() != e.getY()) {
            path.add(curr);

            int[] c = this.mapPrev[curr.getValue()][curr.getKey()];
            curr = new Pair<>(c[0], c[1]);
        }
        endTimes = System.nanoTime();
        super.times += endTimes - startTimes;

        return intermediateMovement.get(new Pair<>( curr, path.getLast() ));
    }
}
