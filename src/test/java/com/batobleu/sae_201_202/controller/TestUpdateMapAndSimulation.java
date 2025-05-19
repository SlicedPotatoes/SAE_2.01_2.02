//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.batobleu.sae_201_202.controller;

import com.batobleu.sae_201_202.exception.InvalidPositionException;
import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.entity.Entity;
import com.batobleu.sae_201_202.model.entity.Sheep;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.model.tile.MapTile;
import com.batobleu.sae_201_202.model.tile.TileEntity;
import com.batobleu.sae_201_202.view.Map;
import java.lang.reflect.Field;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.*;

class TestUpdateMapAndSimulation {
    private static MainController mc;
    private static Field simulationField;
    private static Field mapField;
    private static Simulation simulation;
    private static Map map;

    @BeforeAll
    public static void beforeAll() throws NoSuchFieldException {
        // Initialiser JavaFX
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }

        // Récupération des attributs de classe privée
        simulationField = MainController.class.getDeclaredField("s");
        mapField = MainController.class.getDeclaredField("map");

        // Les rendre accessibles
        simulationField.setAccessible(true);
        mapField.setAccessible(true);
    }

    @BeforeEach
    public void beforeEach() throws IllegalAccessException {
        // Créer de nouvelles instances des attributs
        mc = new MainController();
        simulation = new Simulation(5, 5);
        map = new Map(new Group(), simulation);

        map.addMap();

        // Définir les attributs de classe avec les nouvelles instances
        simulationField.set(mc, simulation);
        mapField.set(mc, map);
    }

    @AfterEach
    public void afterEach() {
        mc = null;
        simulation = null;
        map = null;
    }

    // Permet d'initialiser la carte comme on le souhaite
    // Prends en paramètre des Triplet<Integer, Interger, MapTile> qui représentent les coordonnées x, y et la tile à placer
    private void setupSimulationAndMap(Triplet<Integer, Integer, MapTile>... datas) {
        for(Triplet<Integer, Integer, MapTile> data : datas) {
            int x = data.getValue0();
            int y = data.getValue1();
            MapTile mt = data.getValue2();
            if (mt instanceof TileEntity) {
                simulation.setEntity(mt, x, y);
            } else {
                simulation.getMap()[y][x] = mt;
            }

            map.updateImage(x, y, mt);
        }

    }

    // Permet de vérifier la correspondance entre l'état de la map et l'état passé en paramètre.
    private boolean checkValid(HashMap<Pair<Integer, Integer>, MapTile> mp, Entity wolf, Entity sheep) {
        // Itère sur chaque case de la carte
        for(int y = 0; y < simulation.getNy(); ++y) {
            for(int x = 0; x < simulation.getNx(); ++x) {
                // Récupère dans la HashMap, la case qui est censée être positionnée à cet endroit
                MapTile mt = mp.getOrDefault(Pair.with(x, y), null);
                // Si la case n'a pas été spécifiée, on la définit avec la valeur par défaut quand on génère la grille.
                if (mt == null) {
                    if (x != 0 && x != simulation.getNx() - 1 && y != 0 && y != simulation.getNy() - 1) {
                        mt = MainController.Herb;
                    } else {
                        mt = MainController.Rock;
                    }
                }

                // Récupération de l'url de l'image qui est affichée sur cette case
                ImagePattern ip = (ImagePattern)map.getImages()[y][x].getFill();
                Image i = ip.getImage();
                String url = i.getUrl();

                // Vérifie si l'url correspond à celui de mt.
                if (!url.equals(this.getClass().getResource(mt.getPathIcon()).toExternalForm())) {
                    return false;
                }

                // Dans le cas où la case n'est pas occupée par une entité, vérifie si les données sont correctes dans simulation.
                if (!(mt instanceof TileEntity) && simulation.getMap()[y][x] != mt) {
                    return false;
                }
            }
        }

        Entity simulationWolf = simulation.getWolf();
        Entity simulationSheep = simulation.getSheep();

        // Vérifie si le loup et le mouton ont l'état souhaité dans la simulation
        if((wolf == null && simulationWolf != null) || (wolf != null && simulationWolf == null) ) {
            return false;
        }
        if(wolf != null && (wolf.getX() != simulationWolf.getX() || wolf.getY() != simulationWolf.getY())) {
            return false;
        }
        if((sheep == null && simulationSheep != null) || (sheep != null && simulationSheep == null)) {
            return false;
        }
        if(sheep != null && (sheep.getX() != simulationSheep.getX() || sheep.getY() != simulationSheep.getY())) {
            return false;
        }

        // Aucune contradiction avec les données passées en paramètre et l'état actuel
        return true;
    }

    /*
        Les tests suivants permettent de tester les interactions entre les entités, lorsqu'elles sont positionnées sur la map.
     */

    @Test
    void test01() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 1, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 1, 3, null), null));
    }

    @Test
    void test04() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 1, MainController.Sheep);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Sheep);

        Assertions.assertTrue(this.checkValid(mp, null, new Sheep(1, 1, 2, null)));
    }

    @Test
    void test07() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Sheep));

        mc.updateMapAndSimulation(1, 2, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Sheep);
        mp.put(Pair.with(1, 2), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 2, 3, null), new Sheep(1, 1, 2, null)));
    }

    @Test
    void test09() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Sheep));

        mc.updateMapAndSimulation(1, 1, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 1, 3, null), null));
    }

    @Test
    void test10() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Sheep));

        mc.updateMapAndSimulation(1, 2, MainController.Sheep);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 2), MainController.Sheep);

        Assertions.assertTrue(this.checkValid(mp, null, new Sheep(1, 2, 2, null)));
    }

    @Test
    void test12() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Sheep));

        mc.updateMapAndSimulation(1, 1, MainController.Sheep);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Sheep);

        Assertions.assertTrue(this.checkValid(mp, null, new Sheep(1, 1, 2, null)));
    }

    @Test
    void test13() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf));

        mc.updateMapAndSimulation(1, 2, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 2), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 2, 3, null), null));
    }

    @Test
    void test14() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf));

        mc.updateMapAndSimulation(1, 1, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 1, 3, null), null));
    }

    @Test
    void test16() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf));

        mc.updateMapAndSimulation(1, 2, MainController.Sheep);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Wolf);
        mp.put(Pair.with(1, 2), MainController.Sheep);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 1, 3, null), new Sheep(1, 2, 2, null)));
    }

    @Test
    void test17() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf));

        mc.updateMapAndSimulation(1, 1, MainController.Sheep);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Sheep);

        Assertions.assertTrue(this.checkValid(mp, null, new Sheep(1, 1, 2, null)));
    }

    @Test
    void test19() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf), Triplet.with(1, 2, MainController.Sheep));

        mc.updateMapAndSimulation(1, 3, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 2), MainController.Sheep);
        mp.put(Pair.with(1, 3), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 3, 3, null), new Sheep(1, 2, 2, null)));
    }

    @Test
    void test20() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf), Triplet.with(1, 2, MainController.Sheep));

        mc.updateMapAndSimulation(1, 1, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 2), MainController.Sheep);
        mp.put(Pair.with(1, 1), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 1, 3, null), new Sheep(1, 2, 2, null)));
    }

    @Test
    void test21() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf), Triplet.with(1, 2, MainController.Sheep));

        mc.updateMapAndSimulation(1, 2, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 2), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 2, 3, null), null));
    }

    @Test
    void test22() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf), Triplet.with(1, 2, MainController.Sheep));

        mc.updateMapAndSimulation(1, 3, MainController.Sheep);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 3), MainController.Sheep);
        mp.put(Pair.with(1, 1), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 1, 3, null), new Sheep(1, 3, 2, null)));
    }

    @Test
    void test23() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf), Triplet.with(1, 2, MainController.Sheep));

        mc.updateMapAndSimulation(1, 1, MainController.Sheep);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Sheep);

        Assertions.assertTrue(this.checkValid(mp, null, new Sheep(1, 1, 2, null)));
    }

    @Test
    void test24() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf), Triplet.with(1, 2, MainController.Sheep));

        mc.updateMapAndSimulation(1, 2, MainController.Sheep);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Wolf);
        mp.put(Pair.with(1, 2), MainController.Sheep);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 1, 3, null), new Sheep(1, 2, 2, null)));
    }

    /*
        Les tests suivants permettent de tester le positionnement d'éléments du décor en fonction des positions x et y
     */

    @Test
    void test25() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 0, MainController.Herb);
        });
    }

    @Test
    void test26() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 0, MainController.Cactus);
        });
    }

    @Test
    void test27() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 0, MainController.Poppy);
        });
    }

    @Test
    void test28() throws InvalidPositionException {
        mc.updateMapAndSimulation(0, 0, MainController.Rock);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test29() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 0, MainController.Exit);
        });
    }

    @Test
    void test30() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 1, MainController.Herb);
        });
    }

    @Test
    void test31() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 1, MainController.Cactus);
        });
    }

    @Test
    void test32() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 1, MainController.Poppy);
        });
    }

    @Test
    void test33() throws InvalidPositionException {
        mc.updateMapAndSimulation(0, 1, MainController.Rock);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test34() throws InvalidPositionException {
        mc.updateMapAndSimulation(0, 1, MainController.Exit);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(0, 1), MainController.Exit);

        Assertions.assertTrue(this.checkValid(mp, null, null));
    }

    @Test
    void test35() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 4, MainController.Herb);
        });
    }

    @Test
    void test36() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 4, MainController.Cactus);
        });
    }

    @Test
    void test37() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 4, MainController.Poppy);
        });
    }

    @Test
    void test38() throws InvalidPositionException {
        mc.updateMapAndSimulation(0, 4, MainController.Rock);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test39() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(0, 4, MainController.Exit);
        });
    }

    @Test
    void test40() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(1, 0, MainController.Herb);
        });
    }

    @Test
    void test41() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(1, 0, MainController.Cactus);
        });
    }

    @Test
    void test42() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(1, 0, MainController.Poppy);
        });
    }

    @Test
    void test43() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 0, MainController.Rock);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test44() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 0, MainController.Exit);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 0), MainController.Exit);

        Assertions.assertTrue(this.checkValid(mp, null, null));
    }

    @Test
    void test45() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 1, MainController.Herb);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test46() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 1, MainController.Cactus);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Cactus);

        Assertions.assertTrue(this.checkValid(mp, null, null));
    }

    @Test
    void test47() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 1, MainController.Poppy);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Poppy);

        Assertions.assertTrue(this.checkValid(mp, null, null));
    }

    @Test
    void test48() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 1, MainController.Rock);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Rock);

        Assertions.assertTrue(this.checkValid(mp, null, null));
    }

    @Test
    void test49() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(1, 1, MainController.Exit);
        });
    }

    @Test
    void test50() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(1, 4, MainController.Herb);
        });
    }

    @Test
    void test51() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(1, 4, MainController.Cactus);
        });
    }

    @Test
    void test52() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(1, 4, MainController.Poppy);
        });
    }

    @Test
    void test53() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 4, MainController.Rock);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test54() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 4, MainController.Exit);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 4), MainController.Exit);

        Assertions.assertTrue(this.checkValid(mp, null, null));
    }

    @Test
    void test55() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 0, MainController.Herb);
        });
    }

    @Test
    void test56() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 0, MainController.Cactus);
        });
    }

    @Test
    void test57() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 0, MainController.Poppy);
        });
    }

    @Test
    void test58() throws InvalidPositionException {
        mc.updateMapAndSimulation(4, 0, MainController.Rock);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test59() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 0, MainController.Exit);
        });
    }

    @Test
    void test60() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 1, MainController.Herb);
        });
    }

    @Test
    void test61() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 1, MainController.Cactus   );
        });
    }

    @Test
    void test62() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 1, MainController.Poppy);
        });
    }

    @Test
    void test63() throws InvalidPositionException {
        mc.updateMapAndSimulation(4, 1, MainController.Rock);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test64() throws InvalidPositionException {
        mc.updateMapAndSimulation(4, 1, MainController.Exit);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(4, 1), MainController.Exit);

        Assertions.assertTrue(this.checkValid(mp, null, null));
    }

    @Test
    void test65() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 4, MainController.Herb);
        });
    }

    @Test
    void test66() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 4, MainController.Cactus);
        });
    }

    @Test
    void test67() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 4, MainController.Poppy);
        });
    }

    @Test
    void test68() throws InvalidPositionException {
        mc.updateMapAndSimulation(4, 4, MainController.Rock);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test69() {
        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(4, 4, MainController.Exit);
        });
    }

    /*
        Les tests suivants permettent de tester les interactions entre une entité positionnée sur un décor
     */

    @Test
    void test70() throws InvalidPositionException {
        mc.updateMapAndSimulation(1, 1, MainController.Wolf);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(1, 1), MainController.Wolf);

        Assertions.assertTrue(this.checkValid(mp, new Wolf(1, 1, 3, null), null));
    }

    @Test
    void test71() {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Rock));

        Assertions.assertThrows(InvalidPositionException.class, () -> {
            mc.updateMapAndSimulation(1, 1, MainController.Wolf);
        });
    }

    /*
        Les tests suivants permettent de tester les interactions entre un décor positionné sur une entité
     */

    @Test
    void test72() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Wolf));

        mc.updateMapAndSimulation(1, 1, MainController.Herb);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    @Test
    void test73() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 1, MainController.Sheep));

        mc.updateMapAndSimulation(1, 1, MainController.Herb);

        Assertions.assertTrue(this.checkValid(new HashMap<>(), null, null));
    }

    /*
        Autre test
     */

    @Test
    void test74() throws InvalidPositionException {
        this.setupSimulationAndMap(Triplet.with(1, 4, MainController.Exit));

        mc.updateMapAndSimulation(0, 1, MainController.Exit);

        HashMap<Pair<Integer, Integer>, MapTile> mp = new HashMap<>();
        mp.put(Pair.with(0, 1), MainController.Exit);

        Assertions.assertTrue(this.checkValid(mp, null, null));
    }
}
