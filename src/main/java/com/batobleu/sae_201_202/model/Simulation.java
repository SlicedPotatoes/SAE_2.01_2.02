package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.controller.MainController;
import com.batobleu.sae_201_202.model.algo.PathFinding;
import com.batobleu.sae_201_202.model.exception.IllegalMoveException;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.entity.Sheep;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.model.exception.invalidMap.*;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.model.tile.TileNotReachable;
import javafx.util.Pair;

import java.util.*;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class Simulation {
    public static final int[] dx = {0, 1, 0, -1};
    public static final int[] dy = {1, 0, -1, 0};

    private final int nx, ny;
    private MapTile[][] map;
    private Wolf theWolf;
    private Sheep theSheep;

    private int currRound;
    private int moveLeft;
    private MapTile currEntityTurn;

    private HashMap<MapTile, Integer> counts;

    private List<HistorySimulation> history;
    private int indexAutoMoves;

    private int dLimit;
    private boolean vision;
    private int sumExplorations;
    private long sumTimes;
    private MainController mc;


    public Simulation(int nx, int ny, MainController mc) {
        this.nx = nx;
        this.ny = ny;
        this.mc = mc;

        init();
    }

    public void init() {
        this.map = new MapTile[ny][nx];
        this.theSheep = null;
        this.theWolf = null;

        this.currEntityTurn = SHEEP;
        this.moveLeft = DEFAULT_SPEED_SHEEP;
        this.currRound = 1;

        this.counts = new HashMap<>();

        for (int row = 0; row < ny; row++) {
            for (int col = 0; col < nx; col++) {
                if (row == 0 || col == 0 || row == ny - 1 || col == nx - 1) {
                    this.map[row][col] = ROCK;
                } else {
                    this.map[row][col] = HERB;
                }
            }
        }
    }

    // Getter
    public MapTile[][] getMap() {
        return this.map;
    }
    public Sheep getSheep() {
        return this.theSheep;
    }
    public Wolf getWolf() {
        return this.theWolf;
    }
    public int getNx() {
        return this.nx;
    }
    public int getNy() {
        return this.ny;
    }
    public int getMoveLeft() {
        return this.moveLeft;
    }
    public int getCurrRound() {
        return this.currRound;
    }
    public MapTile getCurrEntityTurn() {
        return this.currEntityTurn;
    }
    public HashMap<MapTile, Integer> getCounts() {
        return this.counts;
    }
    public int getSumExplorations() {
        return this.sumExplorations;
    }
    public long getSumTimes() {
        return this.sumTimes;
    }

    public void setEntity(MapTile entity, int x, int y) {
        if(entity == WOLF) {
            this.theWolf = new Wolf(x, y, DEFAULT_SPEED_WOLF, this);
        }
        else {
            this.theSheep = new Sheep(x, y, DEFAULT_SPEED_SHEEP, this);
        }
    }

    // poor entity 😢
    public void killEntity(Entity entity) {
        if(entity == this.theWolf) {
            this.theWolf = null;
        }
        else {
            this.theSheep = null;
        }
    }

    // Retourne les coordonnées de la sortie
    public List<Integer> findExitMapTile() {
        for(int y = 0; y < this.ny; y++) {
            for(int x = 0; x < this.nx; x++) {
                if(this.map[y][x] == EXIT) {
                    List<Integer> pos = new ArrayList<>();
                    pos.add(x);
                    pos.add(y);

                    return pos;
                }
            }
        }

        return null;
    }

    // Compte le nombre de cellules où une entité peut être positionné
    private int countReachableTile() {
        int count = 0;

        for(int y = 0; y < this.ny; y++) {
            for(int x = 0; x < this.nx; x++) {
                if(! (this.map[y][x] instanceof TileNotReachable)) {
                    count++;
                }
            }
        }

        return count;
    }

    // Compte le nombre de cellules accéssible avec un parcours en profondeur
    private int countWithDFSReachableTile(int x, int y, Set<Pair<Integer, Integer>> visited) {
        visited.add(new Pair<>(x, y));

        int count = 1;

        for(int d = 0; d < 4; d++) {
            int _x = x + dx[d], _y = y + dy[d];

            if(_x < 0 || _x >= this.nx || _y < 0 || _y >= this.ny || this.map[_y][_x] instanceof TileNotReachable) {
                continue;
            }

            if(!visited.contains(new Pair<>(_x, _y))) {
                count += countWithDFSReachableTile(_x, _y, visited);
            }
        }

        return count;
    }

    // Vérifie si une map est valide
    public void isValidMap() throws InvalidMapException {
        List<Integer> exitPos = this.findExitMapTile();

        if(exitPos == null) {
            throw new NoExitException();
        }
        if(this.theWolf == null) {
            throw new NoWolfException();
        }
        if(this.theSheep == null) {
            throw new NoSheepException();
        }

        int crt = this.countReachableTile();
        int countWithDFS = this.countWithDFSReachableTile(exitPos.get(0), exitPos.get(1), new HashSet<>());

        if(crt != countWithDFS) {
            throw new UnconnectedGraphException();
        }
    }

    // Retourne true si la simulation est fini
    public boolean isEnd() {
        return this.theSheep.getIsEaten() || this.theSheep.getIsSafe();
    }

    // Effectue le déplacement de l'entité a qui c'est le tour
    public void move(char dir) throws IllegalMoveException {
        int dx = CHARACTER_DIRECTION.get(dir).getKey();
        int dy = CHARACTER_DIRECTION.get(dir).getValue();

        if(this.currEntityTurn == SHEEP) {
            this.theSheep.move(dx, dy);
        }
        else {
            this.theWolf.move(dx, dy);
        }

        this.moveLeft--;
    }

    // Permet de mettre à jour le compteur de tour et l'entité qui va ce déplacer au prochain tour
    public void endTurn() {
        if(this.moveLeft == 0 && !isEnd()) {
            if(this.currEntityTurn == SHEEP) {
                this.incrementCount(this.map[this.theSheep.getY()][this.theSheep.getX()]);
                this.currEntityTurn = WOLF;
            }
            else {
                this.currEntityTurn = SHEEP;
            }

            this.currRound++;
            this.moveLeft = this.currEntityTurn == SHEEP ? this.theSheep.getSpeed() : this.theWolf.getSpeed();
        }
    }

    // Incrémente le compteur d'élément mangé par le mouton
    public void incrementCount(MapTile mt) {
        this.counts.put(mt, this.counts.getOrDefault(mt, 0) + 1);
    }

    public boolean isChaseMod() {
        return this.isChaseMod(this.dLimit, this.vision);
    }

    // Permet de récupérer les 4 segments d'une case
    private List<Pair<Pair<Double, Double>, Pair<Double, Double>>> getSegments(int x, int y) {
        List<Pair<Pair<Double, Double>, Pair<Double, Double>>> result = new ArrayList<>();

        double size = this.mc == null ? 40 : this.mc.getMap().getSizeSquare();

        // Coin haut gauche → coin bas gauche
        result.add(new Pair<>(new Pair<>(x*size, y*size), new Pair<>(x*size, y*size+size)));
        // Coin bas gauche → coin bas droit
        result.add(new Pair<>(new Pair<>(x*size, y*size+size), new Pair<>(x*size+size, y*size+size)));
        // Coin bas droit → coin haut droit
        result.add(new Pair<>(new Pair<>(x*size+size, y*size+size), new Pair<>(x*size+size, y*size)));
        // Coin haut droit → coin haut gauche
        result.add(new Pair<>(new Pair<>(x*size+size, y*size), new Pair<>(x*size, y*size)));

        return result;
    }

    // Calcul de l'orientation en effectuant un produit vectoriel
    private double orientation(Pair<Double, Double> p, Pair<Double, Double> q, Pair<Double, Double> r) {
        double p0 = p.getKey();
        double p1 = p.getValue();
        double q0 = q.getKey();
        double q1 = q.getValue();
        double r0 = r.getKey();
        double r1 = r.getValue();

        return (q0 - p0) * (r1 - p1) - (q1 - p1) * (r0 - p0);
    }

    // Vérifie si le point r est sur le segment [p, q]
    private boolean onSegment(Pair<Double, Double> p, Pair<Double, Double> q, Pair<Double, Double> r) {
        double p0 = p.getKey();
        double p1 = p.getValue();
        double q0 = q.getKey();
        double q1 = q.getValue();
        double r0 = r.getKey();
        double r1 = r.getValue();

        return Math.min(p0, q0) <= r0 && r0 <= Math.max(p0, q0) && Math.min(p1, q1) <= r1 && r1 <= Math.max(p1, q1);
    }

    // Vérifie si les segments [A, B] et [C, D] sont sécants
    private boolean isSegmentsCut(Pair<Double, Double> A, Pair<Double, Double> B) {
        double size = this.mc == null ? 40 : this.mc.getMap().getSizeSquare();

        Pair<Double, Double> C = new Pair<>(this.theSheep.getX()*size+(size / 2), this.theSheep.getY()*size+(size / 2));
        Pair<Double, Double> D = new Pair<>(this.theWolf.getX()*size+(size / 2), this.theWolf.getY()*size+(size / 2));

        double o1 = orientation(A, B, C);
        double o2 = orientation(A, B, D);
        double o3 = orientation(C, D, A);
        double o4 = orientation(C, D, B);

        // Cas général
        if(o1 * o2 < 0 && o3 * o4 < 0) {
            return true;
        }

        // Cas particuliers (colinéaire ou extrémités touchantes)
        if(o1 == 0 && this.onSegment(A, B, C)) { return true; }
        if(o2 == 0 && this.onSegment(A, B, D)) { return true; }
        if(o3 == 0 && this.onSegment(C, D, A)) { return true; }
        if(o4 == 0 && this.onSegment(C, D, B)) { return true; }

        return false;
    }

    private boolean isChaseMod(int limit, boolean vision) {
        int diffX = this.theSheep.getX() - this.theWolf.getX();
        int diffY = this.theSheep.getY() - this.theWolf.getY();
        int dManhattan = Math.abs(diffX) + Math.abs(diffY);

        // Si la vision à travers les rochers est activée
        if (vision) {
            return dManhattan <= limit;
        }

        // Si la distance de Manhattan est supérieure au seuil, alors on n'est pas en chasemod
        if (dManhattan > limit) {
            return false;
        }

        // Définie le périmètre de recherche des rochers
        int startX = Math.min(this.theSheep.getX(), this.theWolf.getX());
        int endX = Math.max(this.theSheep.getX(), this.theWolf.getX());
        int startY = Math.min(this.theSheep.getY(), this.theWolf.getY());
        int endY = Math.max(this.theSheep.getY(), this.theWolf.getY());

        // Itère dans la zone définie
        for(int y = startY; y <= endY; y++) {
            for(int x = startX; x <= endX; x++) {
                // Si l'élément est un rocher
                if(this.map[y][x] instanceof TileNotReachable) {
                    // On récupère les segments associés à la case.
                    List<Pair<Pair<Double, Double>, Pair<Double, Double>>> segments = this.getSegments(x, y);

                    // Pour chaque segment
                    for(int i = 0; i < segments.size(); i++) {
                        // On vérifie s'il est coupé par le segment [LOUP, MOUTON].
                        if(this.isSegmentsCut(segments.get(i).getKey(), segments.get(i).getValue())) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    // Calcule tous les mouvements de la simulation
    public void autoSimulation(int dManhattan, PathFinding algoSheep, PathFinding algoWolf, boolean vision) throws IllegalMoveException {
        this.dLimit = dManhattan;
        this.vision = vision;
        this.history = new ArrayList<>();
        this.indexAutoMoves = 0;

        this.sumExplorations = 0;
        this.sumTimes = 0;

        this.history.add(new HistorySimulation(this.theWolf, this.theSheep, this.moveLeft, this.currEntityTurn, this.currRound, this.isChaseMod(dManhattan, vision)));

        // Permet d'éviter une boucle infinie si l'algorithme "NoMove" est choisi pour le loup et le mouton
        if(algoSheep == algoWolf && algoSheep == STRING_ALGORITHM_HASHMAP.get("NoMove")) {
            return;
        }

        // Tant que la simulation n'est pas terminée
        while(!this.isEnd()) {
            List<Pair<Integer, Integer>> moves = null;

            Entity e = this.currEntityTurn == SHEEP ? this.theSheep : this.theWolf;
            PathFinding algo = this.currEntityTurn == SHEEP ? algoSheep : algoWolf;
            boolean chaseMod = this.isChaseMod(dManhattan, vision);

            // On récupère les mouvements à effectuer en fonction de la situation (Mouvement en chaseMod ou mouvement aléatoire)
            if(algo == null || !chaseMod) {
                moves = STRING_ALGORITHM_HASHMAP.get("Random").nextMove(this);
            }
            else {
                moves = algo.nextMove(this);
            }

            if(algo != null && this.currEntityTurn == SHEEP && chaseMod) {
                this.sumExplorations += algo.getCountExploredTiles();
                this.sumTimes += algo.getTimes();
            }

            // Pour chaque mouvement
            for (Pair<Integer, Integer> move : moves) {
                e.move(move.getKey(), move.getValue()); // On effectue le mouvement

                // On décrémente le compteur de mouvement restant pour cette entité
                this.moveLeft--;
                // Si c'est la fin du tour, on quitte la boucle
                if(this.moveLeft == 0) {
                    this.endTurn();
                    this.history.add(new HistorySimulation(this.theWolf, this.theSheep, this.moveLeft, this.currEntityTurn, this.currRound, this.isChaseMod(dManhattan, vision)));
                    break;
                }

                // On ajoute l'état actuel à l'historique
                this.history.add(new HistorySimulation(this.theWolf, this.theSheep, this.moveLeft, this.currEntityTurn, this.currRound, this.isChaseMod(dManhattan, vision)));

                // Si l'on n'est plus dans le même mode qu'avant le mouvement
                // On quitte la boucle pour permettre de choisir l'algorithme approprié.
                if(this.isChaseMod(dManhattan, vision) != chaseMod) {
                    break;
                }
            }
        }

        this.setupState(this.history.getFirst());
    }

    private void setupState(HistorySimulation h) {
        this.theWolf = h.getWolf();
        this.theSheep = h.getSheep();
        this.moveLeft = h.getMoveLeft();
        this.currEntityTurn = h.getCurrEntityTurn();
        this.currRound = h.getCr();
    }

    public void setNext() {
        if(this.indexAutoMoves < this.history.size() - 1) {
            this.setupState(this.history.get(++this.indexAutoMoves));
        }
    }

    public void setPrev() {
        if(this.indexAutoMoves > 0) {
            this.setupState(this.history.get(--this.indexAutoMoves));
        }
    }

    public void setLast() {
        this.setupState(this.history.getLast());
        this.indexAutoMoves = this.history.size() - 1;
    }
}
