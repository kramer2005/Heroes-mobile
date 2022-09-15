package br.ufpr.ws_mutantes.views;

import static br.ufpr.ws_mutantes.api.RequestClient.API_URL;
import static br.ufpr.ws_mutantes.api.RequestClient.COOKIE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.net.HttpCookie;
import java.net.URI;

import br.ufpr.ws_mutantes.R;
import br.ufpr.ws_mutantes.api.CredentialsManager;
import br.ufpr.ws_mutantes.api.RequestClient;

public class LoadingActivity extends AppCompatActivity {

    private class ChangeActivityTask extends AsyncTask<Intent, Intent, Intent> {

        @Override
        protected Intent doInBackground(Intent... intents) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return intents[0];
        }

        @Override
        protected void onPostExecute(Intent intent) {
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = new Intent(this, LoginActivity.class);

        if(CredentialsManager.isLoggedIn(this)){
            if(RequestClient.getCookieStore() == null) {
                RequestClient.setCookieStore();
            }
            HttpCookie cookie = new HttpCookie(COOKIE_NAME, CredentialsManager.getUserToken(this));
            RequestClient.getCookieStore().add(URI.create(API_URL), cookie);
            intent = new Intent(this, MainActivity.class);
        }

        ChangeActivityTask activityTask = new ChangeActivityTask();
        activityTask.execute(intent);
    }
}