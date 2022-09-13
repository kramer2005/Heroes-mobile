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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}