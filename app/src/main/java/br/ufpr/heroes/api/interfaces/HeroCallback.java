package br.ufpr.heroes.api.interfaces;

import org.json.JSONException;

import java.util.List;

import br.ufpr.heroes.models.Hero;

public interface HeroCallback {
    public void heroCallback(int status, Hero hero);
}
