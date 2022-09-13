package br.ufpr.heroes.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import br.ufpr.heroes.R;

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
        ChangeActivityTask activityTask = new ChangeActivityTask();
        activityTask.execute(intent);
    }
}