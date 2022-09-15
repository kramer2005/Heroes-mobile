package br.ufpr.ws_mutantes.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import br.ufpr.ws_mutantes.R;
import br.ufpr.ws_mutantes.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    public NestedScrollView mainScroll;
    public View heroesList;
    public Button topAbility1, topAbility2, topAbility3;
    public LinearLayout mainContent;
    public FlowLayout abilitiesLayout;
    SearchFragment searchFragment;
    public TextView registeredMutants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();
    }

    private void setView() {
        this.mainScroll = findViewById(R.id.mainScroll);
        this.heroesList = findViewById(R.id.heroesList);
        this.topAbility1 = findViewById(R.id.ability_1);
        this.topAbility2 = findViewById(R.id.ability_2);
        this.topAbility3 = findViewById(R.id.ability_3);
        this.abilitiesLayout = findViewById(R.id.mutant_abilities);
        this.searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_bar);
        this.mainContent = findViewById(R.id.main_content);
        this.registeredMutants = findViewById(R.id.registered_heroes);
    }

    public void createHero(View v) {
        Intent heroFormIntent = new Intent(this, HeroFormActivity.class);
        startActivity(heroFormIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        searchFragment.searchHero();
        searchFragment.setupDashboard(this);
    }

    @Override
    public void onBackPressed() {
        if(SearchFragment.isSearching) {
            searchFragment.cancelSearch();
        } else {
            super.onBackPressed();
        }
    }
}