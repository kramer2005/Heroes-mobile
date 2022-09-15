package br.ufpr.ws_mutantes.api;

import static br.ufpr.ws_mutantes.api.RequestClient.API_URL;
import static br.ufpr.ws_mutantes.api.RequestClient.COOKIE_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.net.HttpCookie;
import java.net.URI;

import br.ufpr.ws_mutantes.R;

public class CredentialsManager {

    public static SharedPreferences sharedPref;

    public static SharedPreferences getSharedPreferences(Activity activity) {
        if(CredentialsManager.sharedPref == null){
            CredentialsManager.sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        }

        return CredentialsManager.sharedPref;
    }

    public static boolean isLoggedIn(Activity activity) {
        SharedPreferences sharedPref = CredentialsManager.getSharedPreferences(activity);
        return sharedPref.getBoolean(activity.getString(R.string.isUserLoggedIn), false);
    }

    public static void setUserToken(Activity activity, String token, int userId) {
        HttpCookie cookie = new HttpCookie(COOKIE_NAME, token);
        RequestClient.getCookieStore().add(URI.create(API_URL), cookie);
        SharedPreferences sharedPref = CredentialsManager.getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(activity.getString(R.string.isUserLoggedIn), true);
        editor.putString(activity.getString(R.string.userToken), token);
        editor.putInt(activity.getString(R.string.userId), userId);
        editor.apply();
    }

    public static void logout(Activity activity) {
        RequestClient.getCookieStore().removeAll();
        SharedPreferences sharedPref = CredentialsManager.getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(activity.getString(R.string.isUserLoggedIn));
        editor.remove(activity.getString(R.string.userToken));
        editor.remove(activity.getString(R.string.userId));
        editor.apply();
    }

    public static String getUserToken(Activity activity) {
        SharedPreferences sharedPref = CredentialsManager.getSharedPreferences(activity);
        return sharedPref.getString(activity.getString(R.string.userToken), "");
    }

    public static int getUserId(Activity activity) {
        SharedPreferences sharedPref = CredentialsManager.getSharedPreferences(activity);
        return sharedPref.getInt(activity.getString(R.string.userId), -1);
    }

}
