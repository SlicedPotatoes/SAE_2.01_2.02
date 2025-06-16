// Package et imports

package com.batobleu.sae_201_202.model.algo.ACO;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.algo.PathFinding;
import com.batobleu.sae_201_202.model.tile.MapTile;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.batobleu.sae_201_202.controller.MainController.*;
// Classe principale qui implémente l'intterface PathFinding
public class FOURMIEIsaac extends PathFinding {
    // Grille (labyrinthe)
    private MapTile[][] labyrinth;
    // Carte des niveaux de phéromones
    private int[][] labPheromone;
    // Paramètres ACO
    private int numberAnt =100;
    private int nbTourFourmies = 1000;
    private int evaporation= 3;
    private int nbTourEvaporation = 4;
    // Coordonnées de départ et d’arrivée
    private Pair<Integer,Integer> depart;
    private Pair<Integer,Integer> arrive;

    public FOURMIEIsaac() {
    }
    // Retourne les voisins accessibles
    public ArrayList<Pair<Integer, Integer>> voisins(int y, int x) {
        ArrayList<Pair<Integer, Integer>> voisins = new ArrayList<>();
        if (y > 0 && labyrinth[y - 1][x] != ROCK) {
            voisins.add(new Pair<>(y - 1, x));
        }
        if (y < labyrinth.length - 1 && labyrinth[y + 1][x] != ROCK) {
            voisins.add(new Pair<>(y + 1, x));
        }
        if (x > 0 && labyrinth[y][x - 1] != ROCK) {
            voisins.add(new Pair<>(y, x - 1));
        }
        if (x < labyrinth[0].length - 1 && labyrinth[y][x + 1] != ROCK) {
            voisins.add(new Pair<>(y, x + 1));
        }
        return voisins;
    }
    // Déplace les fourmis vers la sortie (arrive)
    public void moveToExit(ArrayList<Ant> Ants){
        for (int i = 0; i < Ants.size(); i++){
            Ant ant = Ants.get(i);
            if(ant.getRecherche() == "A"){
                ArrayList<Pair<Integer, Integer>> voisins = voisins(ant.getCoord().getKey(), ant.getCoord().getValue());
                // Évite les aller-retours immédiats (sauf cas de cul-de-sac)
                int cpt = 0;
                for (int k = 0; k < voisins.size(); k++){
                    if ((ant.getHisto().size() >= 2) && (voisins.size() > 1) && (voisins.get(k - cpt).equals(ant.getHisto().get(ant.getHisto().size()-2)))){
                        voisins.remove(k - cpt);
                        cpt++;
                    }
                }
                // Pondération selon les phéromones
                ArrayList<Integer> weight = new ArrayList<>();
                for (int j = 0; j < voisins.size(); j++){
                    weight.add(labPheromone[voisins.get(j).getKey()][voisins.get(j).getValue()]);
                }
                // Choix du prochain mouvement
                choice(voisins,weight,ant);
                // Si la fourmi atteint l’arrivée, elle change d'objectif
                if(arrive.equals(ant.getCoord())) {
                    ant.setRecherche("D");
                }
            }
        }
    }
    // Retour des fourmis vers le point de départ en déposant des phéromones
    public void moveToStart(ArrayList<Ant> Ants){
        for (int i = 0; i < Ants.size(); i++){
            Ant ant = Ants.get(i);
            if(ant.getRecherche() == "D"){
                // Retour étape par étape
                ant.setCoord(ant.getHisto().get(ant.getHisto().size()-1));
                ant.getHisto().remove(ant.getHisto().size()-1);
                // Dépose de phéromones
                labPheromone[ant.getCoord().getKey()][ant.getCoord().getValue()] += 1;
                // Retour complet au départ, on recommence
                if(depart.equals(ant.getCoord())) {
                    ant.setRecherche("A");
                    ant.getHisto().add(depart);
                }
            }
        }
    }
    // Choix d’un voisin selon la probabilité pondérée par les phéromones
    public void choice(ArrayList<Pair<Integer,Integer>> voisins, ArrayList<Integer> weight,Ant ant){
        ArrayList<Pair<Integer,Integer>> probabilities = new ArrayList<>();
        // Répète les voisins selon le niveau de phéromone pour simuler une distribution
        for (int i = 0; i < voisins.size(); i++){
            for (int j = 0; j < weight.get(i); j++){
                probabilities.add(voisins.get(i));
            }
        }
        // Choix aléatoire pondéré
        ant.setCoord(probabilities.get(getRandomNumber(0, probabilities.size() - 1)));
        // Mémorise le chemin parcouru
        ant.getHisto().add(ant.getCoord());
}
    // Générateur de nombre aléatoire inclusif
    public int getRandomNumber(int min, int max){
        return (int) (Math.random() * ((max - min) + 1)) + min;
}

    public ArrayList<Pair<Integer,Integer>> path(int[][] labPheromone){
        ArrayList<Pair<Integer,Integer>> path = new ArrayList<>();
        Pair<Integer,Integer>curent = new Pair<>(depart.getKey(), depart.getValue());
        boolean finished = false;
        while (!finished){
            ArrayList<Pair<Integer,Integer>> voisins = voisins(curent.getKey(), curent.getValue());
            if(voisins.size()==1){
                path.add(voisins.get(0));
            }
            else{
                Pair<Integer,Integer> maxi = new Pair<>(voisins.get(0).getKey(), voisins.get(0).getValue());
                for (int i = 1; i < voisins.size(); i++){
                    if(labPheromone[voisins.get(i).getKey()][voisins.get(i).getValue()] >= labPheromone[maxi.getKey()][maxi.getValue()]){
                        for (int j = 0; j < path.size(); j++){
                            if (path.get(j).equals(voisins.get(i))){
                                maxi = new Pair<>(voisins.get(i).getKey(), voisins.get(i).getValue());
                            }
                        }
                    }
                }
                path.add(new Pair<>(maxi.getKey(), maxi.getValue()));
                curent = maxi;
            }
            if(curent.getKey() == arrive.getKey() && curent.getValue() == arrive.getValue()){
                finished = true;
            }
        }
        return path;
    }

    // Méthode principale de l’algorithme ACO
    public ArrayList<Pair<Integer,Integer>> ACO (int[][] labPheromone){

        // Initialisation : toutes les cases reçoivent 1 de phéromone
        ArrayList<Ant>ants = new ArrayList<>();
        for (int i =0 ; i<labPheromone.length; i++){
            for (int j =0 ; j<labPheromone[i].length; j++){
                labPheromone[i][j] = 1;
            }
        }

        // Création des fourmis
        for (int i = 0; i <= numberAnt; i++){
            ants.add(new Ant(depart));
        }
        // Exécution de toutes les itérations
        for (int tour = 0; tour <= nbTourFourmies; tour++){
            moveToExit(ants);  // Aller
            moveToStart(ants); // Retour

            // Évaporation des phéromones tous les X tours
            if(tour%nbTourEvaporation==0 && nbTourFourmies > 5){
                for (int i = 0; i < labPheromone.length; i++){
                    for (int j = 0; j < labPheromone.length; j++){
                        if (labPheromone[i][j] - evaporation>0){
                            labPheromone[i][j] -= evaporation;
                        }
                    }
                }
            }
        }
        // Calcul du chemin optimal à partir de la matrice de phéromones
        return path(labPheromone);
    }
    // Méthode obligatoire de PathFinding, appelée pour obtenir les mouvements à effectuer
    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        this.labyrinth = s.getMap();
        this.labPheromone = new int[labyrinth.length][labyrinth[0].length];
        // Détection du tour du mouton ou du loup, et initialisation des positions
        if (s.getCurrEntityTurn() == SHEEP){
            System.out.println("Sheep");
            depart = new Pair<>(s.getSheep().getY(),s.getSheep().getX());
            arrive = new Pair<>(s.findExitMapTile().get(1), s.findExitMapTile().get(0));
        }else {
            System.out.println("Wolf");
            depart = new Pair<>(s.getWolf().getY(),s.getWolf().getX());
            arrive = new Pair<>(s.getSheep().getY(),s.getSheep().getX());
        }
        // Exécution de l’ACO pour obtenir un chemin optimal
        ArrayList<Pair<Integer,Integer>> path = ACO(labPheromone);
        // Transformation du chemin en liste de mouvements relatifs (différences de coordonnées)
        ArrayList<Pair<Integer,Integer>> moves = new ArrayList<>();
        for (int i = 0; i < path.size()-1; i++){
            moves.add(new Pair<>(path.get(i+1).getKey()-path.get(i).getKey(),path.get(i+1).getValue()-path.get(i).getValue()));
        }
        return moves;
    }
}
