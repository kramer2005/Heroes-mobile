package br.ufpr.ws_mutantes.api;

import static br.ufpr.ws_mutantes.api.RequestClient.API_URL;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import br.ufpr.ws_mutantes.api.interfaces.DashCallback;
import br.ufpr.ws_mutantes.models.Dash;


public class DashApi {

    public static void getDash(DashCallback dashCallback, Context ctx) {
        Gson gson = new Gson();

        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                API_URL + "/dash",
                null,
                response -> {
                    JsonElement mJson = JsonParser.parseString(response.toString());
                    Dash dash = gson.fromJson(mJson, Dash.class);
                    dashCallback.dashCallback(200, dash);
                },
                error -> dashCallback.dashCallback(error.networkResponse.statusCode, null)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

}
