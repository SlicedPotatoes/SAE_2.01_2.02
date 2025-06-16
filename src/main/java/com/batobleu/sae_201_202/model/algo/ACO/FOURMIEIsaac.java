package com.batobleu.sae_201_202.model.algo.ACO;

import com.batobleu.sae_201_202.model.Simulation;
import com.batobleu.sae_201_202.model.algo.PathFinding;
import com.batobleu.sae_201_202.model.tile.MapTile;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.batobleu.sae_201_202.controller.MainController.*;

public class FOURMIEIsaac extends PathFinding {
    private MapTile[][] labyrinth;
    private int[][] labPheromone;
    private int numberAnt =10;
    private int nbTourFourmies = 100;
    private int evaporation= 1;
    private int nbTourEvaporation = 5;
    private Pair<Integer,Integer> depart;
    private Pair<Integer,Integer> arrive;

    public FOURMIEIsaac() {
    }

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

    public void moveToExit(ArrayList<Ant> Ants){
        for (int i = 0; i < Ants.size(); i++){
            Ant ant = Ants.get(i);
            if(ant.getRecherche() == "A"){
                ArrayList<Pair<Integer, Integer>> voisins = voisins(ant.getCoord().getKey(), ant.getCoord().getValue());
                int cpt = 0;
                for (int k = 0; k < voisins.size(); k++){
                    if ((ant.getHisto().size() >= 2) && (voisins.size() > 1) && (voisins.get(k - cpt).equals(ant.getHisto().get(ant.getHisto().size()-2)))){
                        voisins.remove(k - cpt);
                        cpt++;
                    }
                }
                ArrayList<Integer> weight = new ArrayList<>();
                for (int j = 0; j < voisins.size(); j++){
                    weight.add(labPheromone[voisins.get(j).getKey()][voisins.get(j).getValue()]);
                }
                choice(voisins,weight,ant);
                if(arrive.equals(ant.getCoord())) {
                    ant.setRecherche("D");
                }
            }
        }
    }

    public void moveToStart(ArrayList<Ant> Ants){
        for (int i = 0; i < Ants.size(); i++){
            Ant ant = Ants.get(i);
            if(ant.getRecherche() == "D"){
                ant.setCoord(ant.getHisto().get(ant.getHisto().size() - 1));
                labPheromone[ant.getCoord().getKey()][ant.getCoord().getValue()] += 1;
                if(depart.equals(ant.getCoord())) {
                    ant.setRecherche("A");
                    ant.getHisto().add(depart);
                }
            }
        }
    }

    public void choice(ArrayList<Pair<Integer,Integer>> voisins, ArrayList<Integer> weight,Ant ant){
        ArrayList<Pair<Integer,Integer>> probabilities = new ArrayList<>();
        for (int i = 0; i < voisins.size(); i++){
            for (int j = 0; j < weight.get(i); j++){
                probabilities.add(voisins.get(i));
            }
        }
        ant.setCoord(probabilities.get(getRandomNumber(0, probabilities.size() - 1)));
        ant.getHisto().add(ant.getCoord());
}

    public int getRandomNumber(int min, int max){
        return (int) (Math.random() * ((max - min) + 1)) + min;
}

    public ArrayList<Pair<Integer,Integer>> ACO (int[][] labPheromone){
        //Init
        ArrayList<Ant>ants = new ArrayList<>();
        for (int i =0 ; i<labPheromone.length; i++){
            for (int j =0 ; j<labPheromone[i].length; j++){
                labPheromone[i][j] = 1;
            }
        }
        for (int i = 0; i <= numberAnt; i++){
            ants.add(new Ant(depart));
        }
        //execution
        for (int tour = 0; tour <= nbTourFourmies; tour++){
            moveToExit(ants);
            moveToStart(ants);
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
       return path(labPheromone);
    }

    public ArrayList<Pair<Integer,Integer>> path(int[][] labPheromone){
        ArrayList<Pair<Integer,Integer>> path = new ArrayList<>();
        Pair<Integer,Integer>curent = depart;
        boolean finished = false;
        while (!finished){
            ArrayList<Pair<Integer,Integer>> voisins = voisins(curent.getKey(), curent.getValue());
            if(voisins.size()==1){
                path.add(voisins.get(0));
            }
            else{
                Pair<Integer,Integer> mini = voisins.get(0);
                for (int i = 1; i < voisins.size(); i++){
                    if(labPheromone[voisins.get(i).getKey()][voisins.get(i).getValue()] < labPheromone[mini.getKey()][mini.getValue()]){}
                        for (int j = 0; j < path.size(); j++){
                            if (path.get(j).equals(voisins.get(i))){
                                mini = voisins.get(i);
                            }
                        }
                }
                path.add(mini);
                curent = mini;
            }
            if(curent.equals(arrive)){
                finished = true;
            }
        }
        return path;
    }

    @Override
    public List<Pair<Integer, Integer>> nextMove(Simulation s) {
        System.out.println("Test1");
        this.labyrinth = s.getMap();
        this.labPheromone = new int[labyrinth.length][labyrinth[0].length];
        System.out.println("Test2");
        if (s.getCurrEntityTurn() == SHEEP){
            depart = new Pair<>(s.getSheep().getY(),s.getSheep().getX());
            arrive = new Pair<>(s.findExitMapTile().get(1), s.findExitMapTile().get(0));
        }else {
            depart = new Pair<>(s.getWolf().getY(),s.getWolf().getX());
            arrive = new Pair<>(s.getSheep().getY(),s.getSheep().getX());
        }
        System.out.println("Test3");
        ArrayList<Pair<Integer,Integer>> path = ACO(labPheromone);
        System.out.println("Test4");
        ArrayList<Pair<Integer,Integer>> moves = new ArrayList<>();
        System.out.println("Test5");
        for (int i = 0; i < path.size()-1; i++){
            moves.add(new Pair<>(path.get(i+1).getKey()-path.get(i).getKey(),path.get(i+1).getValue()-path.get(i).getValue()));

        }
        return moves;
    }
}
