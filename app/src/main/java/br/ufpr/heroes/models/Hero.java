package br.ufpr.heroes.models;

import java.util.List;

public class Hero {
    public int id;
    public String name;
    public String description;
    public int views;
    public String image;
    public String movie;

    public List<String> abilities;

    public Hero(int id, String name, String description, String movie, List<String> abilities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.movie = movie;
        this.abilities = abilities;
    }

    public Hero() {

    }

}
