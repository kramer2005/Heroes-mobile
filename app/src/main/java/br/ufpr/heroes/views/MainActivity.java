package br.ufpr.heroes.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import br.ufpr.heroes.R;

public class MainActivity extends AppCompatActivity {

    public NestedScrollView mainScroll;
    public View heroesList;
    public Button topAbility1, topAbility2, topAbility3;
    public LinearLayout abilitiesLayout, mainContent;
    Fragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainScroll = findViewById(R.id.mainScroll);
        this.heroesList = findViewById(R.id.heroesList);
        this.topAbility1 = findViewById(R.id.top_ability_1);
        this.topAbility2 = findViewById(R.id.top_ability_2);
        this.topAbility3 = findViewById(R.id.top_ability_3);
        this.abilitiesLayout = findViewById(R.id.top_abilities);
        this.searchFragment = getFragmentManager().findFragmentById(R.id.search_bar);
        this.mainContent = findViewById(R.id.main_content);

    }

}