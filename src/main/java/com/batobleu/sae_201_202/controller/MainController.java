package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.model.algo.AlgoKevin.AStarKevin1;
import com.batobleu.sae_201_202.model.algo.AlgoKevin.AStarKevin2;
import com.batobleu.sae_201_202.model.algo.AlgoKevin.BFSKevin;
import com.batobleu.sae_201_202.model.algo.AlgoKevin.DijkstraKevin;
import com.batobleu.sae_201_202.model.algo.BfsMatthis;
import com.batobleu.sae_201_202.model.algo.PathFinding;
import com.batobleu.sae_201_202.model.algo.Random;
import com.batobleu.sae_201_202.model.exception.InvalidPositionException;
import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.tile.*;
import com.batobleu.sae_201_202.view.*;
import com.batobleu.sae_201_202.view.Popup.PopupNewLabyrinth;
import com.batobleu.sae_201_202.view.leftMenu.AutoMenu;
import com.batobleu.sae_201_202.view.leftMenu.LeftMenuManager;
import com.batobleu.sae_201_202.view.leftMenu.MenuSelectItems;
import com.batobleu.sae_201_202.view.leftMenu.MoveMenu;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MainController extends Application {
    // Constante permettant de représenter un élément de la simulation, utilisée pour faire des comparaisons,
    // contient les informations nécessaires à l'affichage
    public static final MapTile ROCK = new TileNotReachable("/Image/Rock.png", "Rocher");
    public static final MapTile CACTUS = new TileHerb("/Image/Cactus.png", "Cactus", 0.5f);
    public static final MapTile HERB = new TileHerb("/Image/Herb.png", "Herbe", 1f);
    public static final MapTile POPPY = new TileHerb("/Image/Flower.png", "Marguerite", 2f);
    public static final MapTile EXIT = new TileExit("/Image/Exit.png", "Sortie");
    public static final MapTile WOLF = new TileEntity("/Image/Wolf.png", "Loup");
    public static final MapTile SHEEP = new TileEntity("/Image/Sheep.png", "Mouton");

    // Dictionnaire pour convertir un caractère vers son équivalent MapTile
    public static final HashMap<Character, MapTile> CHARACTER_MAP_TILE_HASH_MAP = new HashMap<>();
    // Dictionnaire pour convertir un MapTile vers son équivalent caractère
    public static final HashMap<MapTile, Character> MAP_TILE_CHARACTER_HASH_MAP = new HashMap<>();
    // Dictionnaire pour convertir un caractère en déplacement (dx, dy)
    public static final HashMap<Character, Pair<Integer, Integer>> CHARACTER_DIRECTION = new HashMap<>();

    // Liste d'algorithme disponible pour le mouton
    public static final List<String> SHEEP_ALGORITHM = new ArrayList<>();
    // Liste d'algorithme disponible pour le loup
    public static final List<String> WOLF_ALGORITHM = new ArrayList<>();

    // Dictionnaire pour récupérer un algorithme à partir de son nom
    public static final HashMap<String, PathFinding> STRING_ALGORITHM_HASHMAP = new HashMap<>();

    public static final int DEFAULT_SPEED_SHEEP = 2;
    public static final int DEFAULT_SPEED_WOLF = 3;

    private Simulation s;
    private MenuSelectItems msi;
    private Map map;
    private Stage stage;
    private MoveMenu moveMenu;
    private AutoMenu autoMenu;

    private LeftMenuManager lmm;

    // Le type ObjectProperty permet de rendre une variable "observable",
    // on peut y mettre des événements ou faire de l'affichage dynamique
    private ObjectProperty<CurrPage> currPage;
    private BorderPane root;

    public static void main(String[] args) {
        // Remplissage des dictionnaires
        CHARACTER_MAP_TILE_HASH_MAP.put('x', ROCK);
        CHARACTER_MAP_TILE_HASH_MAP.put('c', CACTUS);
        CHARACTER_MAP_TILE_HASH_MAP.put('h', HERB);
        CHARACTER_MAP_TILE_HASH_MAP.put('f', POPPY);
        CHARACTER_MAP_TILE_HASH_MAP.put('s', EXIT);
        CHARACTER_MAP_TILE_HASH_MAP.put('l', WOLF);
        CHARACTER_MAP_TILE_HASH_MAP.put('m', SHEEP);

        MAP_TILE_CHARACTER_HASH_MAP.put(ROCK, 'x');
        MAP_TILE_CHARACTER_HASH_MAP.put(CACTUS, 'c');
        MAP_TILE_CHARACTER_HASH_MAP.put(HERB, 'h');
        MAP_TILE_CHARACTER_HASH_MAP.put(POPPY, 'f');
        MAP_TILE_CHARACTER_HASH_MAP.put(EXIT, 's');
        MAP_TILE_CHARACTER_HASH_MAP.put(WOLF, 'l');
        MAP_TILE_CHARACTER_HASH_MAP.put(SHEEP, 'm');

        CHARACTER_DIRECTION.put('u', new Pair<>(0, -1));
        CHARACTER_DIRECTION.put('d', new Pair<>(0, 1));
        CHARACTER_DIRECTION.put('l', new Pair<>(-1, 0));
        CHARACTER_DIRECTION.put('r', new Pair<>(1, 0));

        SHEEP_ALGORITHM.add("Random");
        SHEEP_ALGORITHM.add("BFSMatthis");
        SHEEP_ALGORITHM.add("BFSKevin");
        SHEEP_ALGORITHM.add("DijkstraKevin");
        SHEEP_ALGORITHM.add("AStarKevin1");
        SHEEP_ALGORITHM.add("AStarKevin2");

        WOLF_ALGORITHM.add("Random");
        WOLF_ALGORITHM.add("BFSMatthis");
        WOLF_ALGORITHM.add("BFSKevin");
        WOLF_ALGORITHM.add("DijkstraKevin");
        WOLF_ALGORITHM.add("AStarKevin1");
        WOLF_ALGORITHM.add("AStarKevin2");

        STRING_ALGORITHM_HASHMAP.put("Random", new Random());
        STRING_ALGORITHM_HASHMAP.put("BFSMatthis", new BfsMatthis());
        STRING_ALGORITHM_HASHMAP.put("BFSKevin", new BFSKevin());
        STRING_ALGORITHM_HASHMAP.put("DijkstraKevin", new DijkstraKevin());
        STRING_ALGORITHM_HASHMAP.put("AStarKevin1", new AStarKevin1());
        STRING_ALGORITHM_HASHMAP.put("AStarKevin2", new AStarKevin2());

        launch();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        PopupNewLabyrinth p = new PopupNewLabyrinth(); // Popup demandant la taille du labyrinthe
        Optional<Pair<Integer, Integer>> result = p.showAndWait();

        // Si le résultat est nul, on arrête le programme
        if(result.isEmpty()) {
            return;
        }

        // On initialise la simulation avec les dimensions récupérées du popup.
        this.s = new Simulation(result.get().getKey(), result.get().getValue());

        _init();
    }

    // Initialisation des éléments de l'UI
    public void _init() {
        this.root = new BorderPane();

        Scene scene = new Scene(root, 1280, 720);
        this.stage.setTitle("Mange moi si tu peux !");
        this.stage.setScene(scene);

        this.lmm = new LeftMenuManager(this);

        this.map = new Map(this);
        this.msi = new MenuSelectItems(this);

        new MenuBarUp(this);

        this.map.addMap();
        new InformationDebug(this);
        this.moveMenu = new MoveMenu(this);
        this.autoMenu = new AutoMenu(this, scene);

        this.currPage = new SimpleObjectProperty<>();
        EventManager.addEventChangePage(this);

        this.setCurrPage(CurrPage.SetupDecor);

        stage.show();
    }

    // Getter
    public Simulation getSimulation() {
        return this.s;
    }
    public BorderPane getRoot() {
        return this.root;
    }
    public Map getMap() {
        return this.map;
    }
    public MenuSelectItems getMsi() {
        return this.msi;
    }
    public Stage getStage() { return this.stage; }
    public MoveMenu getMoveMenu() {
        return this.moveMenu;
    }
    public LeftMenuManager getLmm() {
        return this.lmm;
    }
    public ObjectProperty<CurrPage> getCurrPage() {
        return this.currPage;
    }
    public AutoMenu getAutoMenu() { return this.autoMenu; }

    public void setCurrPage(CurrPage cp) {
        this.currPage.set(cp);
    }

    // Méthode permettant de mettre à jour la simulation est l'affichage, pendant la configuration du labyrinthe.
    public void updateMapAndSimulation(int x, int y, MapTile selectedItem) throws InvalidPositionException {
        // Si on tente de placer un élément à une position incorrecte (par exemple une sortie au milieu du terrain).
        if(!selectedItem.isValidPosition(x, y, this.s.getNx(), this.s.getNy(), this.s.getMap()[y][x])) {
            throw new InvalidPositionException();
        }

        /*
        * Dans le cas où selectedItem représente une entité :
        *  - entity1 sera l'entité, dans simulation, qui correspond à l'entité dans selectedItem
        *  - entity2 sera l'autre
        * Sinon:
        *  - entity1 sera l'entité, dans la simulation, qui correspond au loup
        *  - entity2 sera le mouton
        */
        Entity entity1 = selectedItem instanceof TileEntity ? (selectedItem == WOLF ? this.s.getWolf() : this.s.getSheep()) : this.s.getWolf();
        Entity entity2 = selectedItem instanceof TileEntity ? (selectedItem == WOLF ? this.s.getSheep() : this.s.getWolf()) : this.s.getSheep();

        // Si on a sélectionné une entité
        if(selectedItem instanceof TileEntity) {
            // Si on positionne une entité sur une autre, on supprime la seconde entité
            if(entity2 != null && x == entity2.getX() && y == entity2.getY()) {
                this.s.killEntity(entity2);
            }
            // Si on déplace une entité, on fait apparaître l'image du décor correspondant
            if(entity1 != null) {
                int _x = entity1.getX(), _y = entity1.getY();
                this.map.updateImage(_x, _y, this.s.getMap()[_y][_x]);
            }

            // On met à jour l'entité dans la simulation
            this.s.setEntity(selectedItem, x, y);
        }
        else {
            // Si le décor se trouve sur une entité, on supprime l'entité correspondante dans la simulation
            if(entity1 != null && x == entity1.getX() && y == entity1.getY()){
                this.s.killEntity(entity1);
            }
            else if(entity2 != null && x == entity2.getX() && y == entity2.getY()){
                this.s.killEntity(entity2);
            }

            // Cas de la sortie
            if(selectedItem instanceof TileExit) {
                List<Integer> pos = this.s.findExitMapTile();

                if(pos != null) {
                    this.s.getMap()[pos.get(1)][pos.get(0)] = ROCK;
                    this.map.updateImage(pos.get(0), pos.get(1), ROCK);
                }
            }

            // On met à jour la map avec le décor correspondant
            this.s.getMap()[y][x] = selectedItem;
        }

        // On met à jour l'affichage
        this.map.updateImage(x, y, selectedItem);
    }

    // Méthode permettant d'initialiser l'affichage et les données à partir d'un fichier.
    public void initWithFile(List<String> lineFiles) {
        int nx = lineFiles.getFirst().length();
        int ny = lineFiles.size();

        this.s = new Simulation(nx, ny);

        for(int y = 0; y < ny; y++) {
            for(int x = 0; x < nx; x++) {
                MapTile mt = CHARACTER_MAP_TILE_HASH_MAP.get(lineFiles.get(y).charAt(x));

                if(mt instanceof TileEntity) {
                    this.s.setEntity(mt, x, y);
                }
                else {
                    this.s.getMap()[y][x] = mt;
                }
            }
        }

        _init();
    }


}