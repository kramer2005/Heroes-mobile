package br.ufpr.heroes.api;

import static br.ufpr.heroes.api.RequestClient.API_URL;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufpr.heroes.models.AuthAnswer;
import br.ufpr.heroes.models.User;
import br.ufpr.heroes.views.LoginActivity;

public class Public {

    public static void login(User user, LoginActivity activity) throws JSONException {
        Gson gson = new Gson();
        Context ctx = activity.getApplicationContext();
        String jsonObject = gson.toJson(user);
        JSONObject input = new JSONObject(jsonObject);

        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                API_URL + "/public/login",
                input,
                response -> {
                    JsonElement mJson = JsonParser.parseString(response.toString());
                    AuthAnswer authAnswer = gson.fromJson(mJson, AuthAnswer.class);
                    CredentialsManager.setUserToken(activity, authAnswer.response);
                    Toast.makeText(ctx, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    activity.successFullLogin();
                },
                error -> activity.loginFailure(error.networkResponse.statusCode)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    public static void register(User user, LoginActivity activity) throws JSONException {
        Gson gson = new Gson();
        Context ctx = activity.getApplicationContext();
        String jsonObject = gson.toJson(user);
        JSONObject input = new JSONObject(jsonObject);

        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                API_URL + "/public/register",
                input,
                response -> {
                    AuthAnswer authAnswer = gson.fromJson(gson.toJson(response), AuthAnswer.class);
                    CredentialsManager.setUserToken(activity, authAnswer.response);
                    Toast.makeText(ctx, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();
                    activity.successFullLogin();
                },
                error -> activity.loginFailure(error.networkResponse.statusCode)
        );

        RequestClient.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

}
