package br.ufpr.ws_mutantes.api;

import static br.ufpr.ws_mutantes.api.RequestClient.API_URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufpr.ws_mutantes.api.interfaces.HeroCallback;
import br.ufpr.ws_mutantes.api.interfaces.HeroesCallback;
import br.ufpr.ws_mutantes.models.Hero;

public class HeroesApi {

    public static void getHeroes(HeroesCallback callable, Context ctx, @Nullable String search, @Nullable String ability) {
        Gson gson = new Gson();
        JsonArrayRequest jsonArrayRequest;
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                API_URL + "/hero/find" + (search != null ? "/" + search : "")
                        + (ability != null ? "?ability=" + ability : ""),
                null,
                response -> {
                    List<Hero> list = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
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

    public static void postHero(HeroCallback callable, Context ctx, Hero hero, Bitmap bmp) throws JSONException {
        Gson gson = new Gson();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, API_URL + "/hero/", response -> {
            callable.heroCallback(response.statusCode, null);
        }, error -> {
            callable.heroCallback(error.networkResponse.statusCode, null);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", hero.name);
                params.put("description", hero.description);
                params.put("movie", hero.movie);
                params.put("abilities", gson.toJson(hero.abilities));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                params.put("image", new DataPart("file_avatar.jpg", byteArray, "image/jpeg"));

                return params;
            }
        };

        RequestClient.getInstance(ctx).addToRequestQueue(multipartRequest);
    }

    public static void putHero(HeroCallback callable, Context ctx, Hero hero, Bitmap bmp) throws JSONException {
        Gson gson = new Gson();
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.PUT, API_URL + "/hero/" + hero.id, response -> {
            callable.heroCallback(response.statusCode, null);
        }, Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(hero.id));
                params.put("name", hero.name);
                params.put("description", hero.description);
                params.put("movie", hero.movie);
                params.put("abilities", gson.toJson(hero.abilities));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (bmp != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    params.put("image", new DataPart("file_avatar.jpg", byteArray, "image/jpeg"));
                }

                return params;
            }
        };

        RequestClient.getInstance(ctx).addToRequestQueue(multipartRequest);
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
