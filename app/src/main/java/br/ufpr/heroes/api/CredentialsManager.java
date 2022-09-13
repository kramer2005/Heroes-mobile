package br.ufpr.heroes.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import br.ufpr.heroes.R;

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

    public static void setUserToken(Activity activity, String token) {
        SharedPreferences sharedPref = CredentialsManager.getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(activity.getString(R.string.isUserLoggedIn), true);
        editor.putString(activity.getString(R.string.userToken), token);
        editor.apply();
    }

    public static void logout(Activity activity) {
        SharedPreferences sharedPref = CredentialsManager.getSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(activity.getString(R.string.isUserLoggedIn));
        editor.remove(activity.getString(R.string.userToken));
        editor.apply();
    }

    public static String getUserToken(Activity activity, String token) {
        SharedPreferences sharedPref = CredentialsManager.getSharedPreferences(activity);
        return sharedPref.getString(activity.getString(R.string.userToken), "");
    }

}
