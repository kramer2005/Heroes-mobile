package br.ufpr.heroes.api.interfaces;

import java.util.List;

import br.ufpr.heroes.models.Hero;

public interface HeroesCallback {
    public void getHeroesCallback(int status, List<Hero> dash);
}
