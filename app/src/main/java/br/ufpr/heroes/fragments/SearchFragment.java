package br.ufpr.heroes.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.ufpr.heroes.R;
import br.ufpr.heroes.adapters.SpinnerAdapter;
import br.ufpr.heroes.api.AbilitiesApi;
import br.ufpr.heroes.api.CredentialsManager;
import br.ufpr.heroes.api.HeroesApi;
import br.ufpr.heroes.models.Hero;
import br.ufpr.heroes.utils.Debouncer;
import br.ufpr.heroes.views.LoginActivity;
import br.ufpr.heroes.views.MainActivity;

public class SearchFragment extends Fragment implements Debouncer.Callback<Editable> {

    public static List<Hero> heroes = new ArrayList<>();

    ImageView exitBtn;
    public static HeroCardRecyclerViewAdapter adapter;
    public SearchFragment() {}
    private String search;
    private String actualAbility;
    private final Debouncer<Editable> debouncer = new Debouncer<Editable>(500, this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();

        assert activity != null;

        EditText editText = view.findViewById(R.id.searchInput);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                debouncer.consume(editable);
            }
        });
        setupAbilitySelector(view, activity);
        setupExitBtn();
    }

    private void searchHero() {
        HeroesApi.getHeroes((int status, List<Hero> heroes) -> updateHeroesList(heroes), getContext(), search, actualAbility);
    }

    private void setupAbilitySelector(@NonNull View view, MainActivity activity) {
        Spinner mSpinner = view.findViewById(R.id.abilitySpinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                actualAbility = i > 0 ? adapterView.getItemAtPosition(i).toString() : null;
                searchHero();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        assert activity != null;
        activity.mainScroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int[] location = new int[2];
            int[] listLocation = new int[2];
            activity.mainScroll.getLocationOnScreen(location);
            activity.heroesList.getLocationOnScreen(listLocation);
            if(listLocation[1] <= location[1] + 72) {
                mSpinner.setVisibility(View.VISIBLE);
            }
        });

        AbilitiesApi.getAbilities((int status, List<String> list) -> {
            list.add(0, "Habilidade");
            SpinnerAdapter mArrayAdapter = new SpinnerAdapter(requireContext(), R.layout.spinner_list, list);
            mArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
            mSpinner.setAdapter(mArrayAdapter);
        }, getContext());
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void updateHeroesList(List<Hero> heroes) {
        SearchFragment.heroes.clear();
        SearchFragment.heroes.addAll(heroes);
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setupExitBtn() {
        exitBtn = requireActivity().findViewById(R.id.logoutBtn);

        exitBtn.setOnClickListener(view1 -> {
            CredentialsManager.logout(getActivity());
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
            requireActivity().finish();
        });
    }

    @Override
    public void onEmit(Editable key) {
        search = key.toString();
        searchHero();
    }
}