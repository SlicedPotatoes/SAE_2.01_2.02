package com.batobleu.sae_201_202.model.algo.ACO;

import javafx.util.Pair;

import java.util.ArrayList;

public class Ant {
    private Pair<Integer, Integer> coord;
    private ArrayList<Pair<Integer,Integer>> histo;
    private String Recherche;

    public Ant(Pair<Integer,Integer> coord) {
        this.coord = coord;
        this.histo = new ArrayList<>();
        this.histo.add(coord);
        this.Recherche = "A";
    }

    public Pair<Integer,Integer> getCoord() {
        return coord;
    }

    public ArrayList<Pair<Integer,Integer>> getHisto() {
        return histo;
    }

    public String getRecherche() {
        return Recherche;
    }

    public void setCoord(Pair<Integer, Integer> coord) {
        this.coord = coord;
    }

    public void setRecherche(String recherche) {
        Recherche = recherche;
    }
}


