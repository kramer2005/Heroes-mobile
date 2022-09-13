package br.ufpr.heroes.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

import br.ufpr.heroes.R;
import br.ufpr.heroes.api.HeroesApi;
import br.ufpr.heroes.models.Hero;

public class MainActivity extends AppCompatActivity {
    public void onGetHero(int status, Hero hero) {
        System.out.println(status);
        try {
            HeroesApi.postHero(this::onPostHero, this.getApplicationContext(), hero);
            HeroesApi.putHero(this::onPutHero, this.getApplicationContext(), hero);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onPostHero(int status, Hero hero) {
        if(status == 409) {
            Toast.makeText(this, "Herói já cadastrado", Toast.LENGTH_SHORT).show();
        }
    }

    public void onPutHero(int status, Hero hero) {
        System.out.println(status);

    }

    public void onDeleteHero(int status, Hero hero) {
        System.out.println(status);

    }

    public void heroesCallback(int status, List<Hero> heroes) {
        System.out.println(status);

        HeroesApi.getHero(this::onGetHero, this.getApplicationContext(), 1);

        HeroesApi.deleteHero(this::onDeleteHero, this.getApplicationContext(), heroes.get(heroes.size() - 1).id);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context ctx = this.getApplicationContext();

        HeroesApi.getHeroes(this::heroesCallback, ctx, null, null);
    }
}