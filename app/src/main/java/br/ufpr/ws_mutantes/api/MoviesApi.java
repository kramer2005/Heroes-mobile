package br.ufpr.ws_mutantes.api;

import static br.ufpr.ws_mutantes.api.RequestClient.API_URL;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.ufpr.ws_mutantes.api.interfaces.MoviesCallback;

public class MoviesApi {

    public static void getMovies(MoviesCallback callable, Context ctx) {
        JsonArrayRequest jsonArrayRequest;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                API_URL + "/movies",
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
                    callable.moviesCallback(200, list);
                },
                error -> callable.moviesCallback(error.networkResponse.statusCode, null)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonArrayRequest);
    }

}
