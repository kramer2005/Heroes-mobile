package br.ufpr.ws_mutantes.api.interfaces;

import java.util.List;

public interface MoviesCallback {
    public void moviesCallback(int status, List<String> list);
}
