package com.batobleu.sae_201_202.model;

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

    public Simulation(int nx, int ny) {
        this.nx = nx;
        this.ny = ny;

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
        return this.isChaseMod(this.dLimit);
    }
    private boolean isChaseMod(int limit) {
        int diffX = this.theSheep.getX() - this.theWolf.getX();
        int diffY = this.theSheep.getY() - this.theWolf.getY();
        return Math.abs(diffX) + Math.abs(diffY) <= limit;
    }

    public void autoSimulation(int dManhattan, PathFinding algoSheep, PathFinding algoWolf) throws IllegalMoveException {
        this.dLimit = dManhattan;

        this.history = new ArrayList<>();
        this.indexAutoMoves = 0;

        this.history.add(new HistorySimulation(this.theWolf, this.theSheep, this.moveLeft, this.currEntityTurn, this.currRound, this.isChaseMod(dManhattan)));

        while(!this.isEnd()) {
            List<Pair<Integer, Integer>> moves = null;

            Entity e = this.currEntityTurn == SHEEP ? this.theSheep : this.theWolf;

            PathFinding algo = this.currEntityTurn == SHEEP ? algoSheep : algoWolf;

            boolean chaseMod = this.isChaseMod(dManhattan);

            if(algo == null || !chaseMod) {
                moves = STRING_ALGORITHM_HASHMAP.get("Random").nextMove(this);
            }
            else {
                moves = algo.nextMove(this);
            }

            for (Pair<Integer, Integer> move : moves) {
                System.out.println(e.getClass() + " Mouvement: " + move);
                e.move(move.getKey(), move.getValue());

                this.moveLeft--;
                if(this.moveLeft == 0) {
                    this.endTurn();
                    this.history.add(new HistorySimulation(this.theWolf, this.theSheep, this.moveLeft, this.currEntityTurn, this.currRound, this.isChaseMod(dManhattan)));
                    break;
                }

                this.history.add(new HistorySimulation(this.theWolf, this.theSheep, this.moveLeft, this.currEntityTurn, this.currRound, this.isChaseMod(dManhattan)));

                if(this.isChaseMod(dManhattan) != chaseMod) {
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
}
