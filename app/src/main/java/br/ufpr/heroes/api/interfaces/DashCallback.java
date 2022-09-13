package br.ufpr.heroes.api.interfaces;

import br.ufpr.heroes.models.Dash;

public interface DashCallback {
    public void dashCallback(int status, Dash dash);
}
