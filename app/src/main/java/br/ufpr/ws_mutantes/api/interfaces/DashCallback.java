package br.ufpr.ws_mutantes.api.interfaces;

import br.ufpr.ws_mutantes.models.Dash;

public interface DashCallback {
    public void dashCallback(int status, Dash dash);
}
