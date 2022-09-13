package br.ufpr.heroes.models;

import java.util.List;

public class Hero {
    public int id;
    public String name;
    public String description;
    public String movie;
    public String image;
    public int views;

    public List<String> powers;

    public Hero(int id, String name, String description, String movie, List<String> powers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.movie = movie;
        this.powers = powers;
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
        return powers;
    }

    public void setPowers(List<String> poderes) {
        this.powers = powers;
    }

}
