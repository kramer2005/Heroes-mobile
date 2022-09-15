package br.ufpr.ws_mutantes.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apmem.tools.layouts.FlowLayout;

import br.ufpr.ws_mutantes.R;
import br.ufpr.ws_mutantes.api.CredentialsManager;
import br.ufpr.ws_mutantes.api.HeroesApi;
import br.ufpr.ws_mutantes.api.RequestClient;
import br.ufpr.ws_mutantes.models.Hero;

public class MutantActivity extends AppCompatActivity {
    int mutantId;
    TextView mutantName, mutantHistory, mutantMovie;
    ImageView mutantImage, goBack;
    FlowLayout mutantAbilities;
    FloatingActionButton editBtn, removeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutant);

        Intent intent = getIntent();
        int userId = CredentialsManager.getUserId(this);
        mutantId = intent.getIntExtra("mutantId", 0);
        setup();

        goBack.setOnClickListener((l) -> {
            finish();
        });

        HeroesApi.getHero((int status, Hero hero) -> {
            mutantName.setText(hero.name);
            mutantHistory.setText(hero.description);
            mutantMovie.setText(hero.movie);
            Glide.with(getApplicationContext())
                    .load(RequestClient.API_URL + "/images/" + hero.image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(mutantImage);
            for(int i = 0; i < hero.abilities.size(); i++) {
                TextView abilityTextView = (TextView) mutantAbilities.getChildAt(i);
                abilityTextView.setVisibility(View.VISIBLE);
                abilityTextView.setText(hero.abilities.get(i));
            }
            if(hero.userId == userId) {
                editBtn.setVisibility(View.VISIBLE);
                removeBtn.setVisibility(View.VISIBLE);
            }
        }, this.getApplicationContext(), mutantId);
    }

    private void setup() {
        mutantName = findViewById(R.id.mutantName);
        mutantHistory = findViewById(R.id.mutantHistory);
        mutantImage = findViewById(R.id.mutantImage);
        mutantMovie = findViewById(R.id.mutantMovie);
        mutantAbilities = findViewById(R.id.mutant_abilities);
        goBack = findViewById(R.id.goBack);
        editBtn = findViewById(R.id.editButton);
        removeBtn = findViewById(R.id.removeButton);
    }

    public void editMutant(View v) {
        Intent editIntent = new Intent(this, HeroFormActivity.class);
        editIntent.putExtra("mutantId", mutantId);
        startActivity(editIntent);
    }

    public void removeMutant(View v) {
        HeroesApi.deleteHero((int status, Hero h) -> {
            finish();
        }, getApplicationContext(), mutantId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setup();

        HeroesApi.getHero((int status, Hero hero) -> {
            mutantName.setText(hero.name);
            mutantHistory.setText(hero.description);
            mutantMovie.setText(hero.movie);
            Glide.with(getApplicationContext())
                    .load(RequestClient.API_URL + "/images/" + hero.image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(mutantImage);
            for(int i = 0; i < hero.abilities.size(); i++) {
                TextView abilityTextView = (TextView) mutantAbilities.getChildAt(i);
                abilityTextView.setVisibility(View.VISIBLE);
                abilityTextView.setText(hero.abilities.get(i));
            }
        }, this.getApplicationContext(), mutantId);
    }
}