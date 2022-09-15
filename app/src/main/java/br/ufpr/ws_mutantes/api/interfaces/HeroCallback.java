package br.ufpr.ws_mutantes.api.interfaces;

import br.ufpr.ws_mutantes.models.Hero;

public interface HeroCallback {
    public void heroCallback(int status, Hero hero);
}
