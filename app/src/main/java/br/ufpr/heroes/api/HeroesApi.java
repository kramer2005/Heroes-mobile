package br.ufpr.heroes.api;

import static br.ufpr.heroes.api.RequestClient.API_URL;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.ufpr.heroes.api.interfaces.HeroCallback;
import br.ufpr.heroes.api.interfaces.HeroesCallback;
import br.ufpr.heroes.models.Hero;

public class HeroesApi {

    public static void getHeroes(HeroesCallback callable, Context ctx, @Nullable String search, @Nullable String ability) {
        Gson gson = new Gson();
        JsonArrayRequest jsonArrayRequest;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                API_URL + "/hero/find" + (search != null? "/" + search : "")
                        + (ability != null ? "?ability=" + ability : ""),
                null,
                response -> {
                    List<Hero> list = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++){
                        try {
                            JsonElement mJson = JsonParser.parseString(response.getString(i));
                            Hero hero = gson.fromJson(mJson, Hero.class);
                            list.add(hero);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callable.getHeroesCallback(200, list);
                },
                error -> callable.getHeroesCallback(error.networkResponse.statusCode, null)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonArrayRequest);
    }

    public static void getHero(HeroCallback callback, Context ctx, int id) {
        Gson gson = new Gson();

        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                API_URL + "/hero/" + id,
                null,
                response -> {
                    JsonElement mJson = JsonParser.parseString(response.toString());
                    Hero hero = gson.fromJson(mJson, Hero.class);
                    callback.heroCallback(200, hero);
                },
                error -> callback.heroCallback(error.networkResponse.statusCode, null)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    public static void postHero(HeroCallback callable, Context ctx, Hero hero) throws JSONException {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(hero);
        JSONObject input = new JSONObject(jsonObject);

        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                API_URL + "/hero",
                input,
                response -> callable.heroCallback(200, null),
                error -> callable.heroCallback(error.networkResponse.statusCode, null)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    public static void putHero(HeroCallback callable, Context ctx, Hero hero) throws JSONException {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(hero);
        JSONObject input = new JSONObject(jsonObject);

        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                API_URL + "/hero/" + hero.id,
                input,
                response -> callable.heroCallback(200, null),
                error -> callable.heroCallback(error.networkResponse.statusCode, null)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    public static void deleteHero(HeroCallback callable, Context ctx, int id) {
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                API_URL + "/hero/" + id,
                null,
                response -> callable.heroCallback(200, null),
                error -> callable.heroCallback(error.networkResponse.statusCode, null)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }
}
