package br.ufpr.heroes.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentContainerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import br.ufpr.heroes.R;

public class MainActivity extends AppCompatActivity {

    public NestedScrollView mainScroll;
    public View heroesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainScroll = findViewById(R.id.mainScroll);
        this.heroesList = findViewById(R.id.heroesList);
    }

}