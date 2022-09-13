package br.ufpr.heroes.api;

import static br.ufpr.heroes.api.RequestClient.API_URL;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.ufpr.heroes.api.interfaces.AbilitiesCallback;

public class AbilitiesApi {

    public static void getAbilities(AbilitiesCallback callable, Context ctx) {
        JsonArrayRequest jsonArrayRequest;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                API_URL + "/abilities",
                null,
                response -> {
                    List<String> list = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++){
                        try {
                            list.add(response.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callable.abilitiesCallback(200, list);
                },
                error -> callable.abilitiesCallback(error.networkResponse.statusCode, null)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonArrayRequest);
    }

}
