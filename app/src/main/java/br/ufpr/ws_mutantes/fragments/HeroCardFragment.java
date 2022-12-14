package br.ufpr.ws_mutantes.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.ufpr.ws_mutantes.R;
import br.ufpr.ws_mutantes.adapters.HeroCardRecyclerViewAdapter;
import br.ufpr.ws_mutantes.api.HeroesApi;
import br.ufpr.ws_mutantes.models.Hero;

/**
 * A fragment representing a list of Items.
 */
public class HeroCardFragment extends Fragment {

    View view;

    public HeroCardFragment() {
    }

    @SuppressWarnings("unused")
    public static HeroCardFragment newInstance(int columnCount) {
        HeroCardFragment fragment = new HeroCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hero_card_list, container, false);

        HeroesApi.getHeroes((int status, List<Hero> items) -> {
            if (view instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setAdapter(new HeroCardRecyclerViewAdapter());

                SearchFragment.adapter = (HeroCardRecyclerViewAdapter) recyclerView.getAdapter();
                SearchFragment.updateHeroesList(items);
            }
        }, view.getContext(), null, null);

        return view;
    }
}