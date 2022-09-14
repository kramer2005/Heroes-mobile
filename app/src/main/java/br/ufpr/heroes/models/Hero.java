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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return name;
    }

    public void setNome(String nome) {
        this.name = nome;
    }

    public List<String> getPowers() {
        return abilities;
    }

    public void setPowers(List<String> poderes) {
        this.abilities = poderes;
    }

}
