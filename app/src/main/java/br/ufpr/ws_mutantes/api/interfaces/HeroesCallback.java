package br.ufpr.ws_mutantes.api.interfaces;

import java.util.List;

import br.ufpr.ws_mutantes.models.Hero;

public interface HeroesCallback {
    public void getHeroesCallback(int status, List<Hero> heroes);
}
