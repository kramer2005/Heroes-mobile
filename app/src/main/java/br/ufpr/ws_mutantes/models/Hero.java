package br.ufpr.ws_mutantes.models;

import androidx.annotation.Nullable;

import java.util.List;

public class Hero {
    public int id;
    public String name;
    public String description;
    public int views;
    public String image;
    public String movie;
    public int userId;

    public List<String> abilities;

    public Hero(@Nullable int id, String name, String description, String movie, List<String> abilities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.movie = movie;
        this.abilities = abilities;
    }

}
