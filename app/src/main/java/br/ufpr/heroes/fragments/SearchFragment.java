package br.ufpr.heroes.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.ufpr.heroes.R;
import br.ufpr.heroes.adapters.SpinnerAdapter;
import br.ufpr.heroes.api.AbilitiesApi;
import br.ufpr.heroes.api.CredentialsManager;
import br.ufpr.heroes.api.DashApi;
import br.ufpr.heroes.api.HeroesApi;
import br.ufpr.heroes.models.Dash;
import br.ufpr.heroes.models.Hero;
import br.ufpr.heroes.utils.Debouncer;
import br.ufpr.heroes.views.LoginActivity;
import br.ufpr.heroes.views.MainActivity;

public class SearchFragment extends Fragment {

    public static List<Hero> heroes = new ArrayList<>();

    public static HeroCardRecyclerViewAdapter adapter;
    public SearchFragment() {}
    private String search;
    private String actualAbility;
    private final Debouncer<Editable> debouncer = new Debouncer<>(500, this::onEmit);
    public List<String> abilities;
    private LinearLayout mainContent;
    private ImageView goBackButton, exitButton, searchButton;
    int[] location = new int[2];
    int[] listLocation = new int[2];

    // Main Activity components
    Spinner mSpinner;
    NestedScrollView mainScroll;

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
        mSpinner = view.findViewById(R.id.abilitySpinner);
        assert activity != null;

        setupGoBackButton();
        setupDashboard(activity);
        setupSearch(view);
        setupAbilitySelector(activity);
        setupExitBtn();
    }

    private void setupGoBackButton() {
        searchButton = requireActivity().findViewById(R.id.searchIcon);
        goBackButton = requireActivity().findViewById(R.id.goBackSearch);
        goBackButton.setOnClickListener((v) -> cancelSearch());
    }

    private void setupSearch(@NonNull View view) {
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
    }

    private void setupDashboard(MainActivity activity) {
        mainContent = activity.mainContent;
        DashApi.getDash((int status, Dash dash) -> {
            View.OnClickListener onClickListener = (View v) -> {
                Button b = (Button)v;
                mSpinner.setSelection(abilities.indexOf(b.getText().toString()));
                setupSearch();
            };
            activity.topAbility1.setText(dash.topThreeAbilities.get(0));
            activity.topAbility2.setText(dash.topThreeAbilities.get(1));
            activity.topAbility3.setText(dash.topThreeAbilities.get(2));
            activity.topAbility1.setOnClickListener(onClickListener);
            activity.abilitiesLayout.setVisibility(View.VISIBLE);
            activity.topAbility1.setOnClickListener(onClickListener);
            activity.topAbility2.setOnClickListener(onClickListener);
            activity.topAbility3.setOnClickListener(onClickListener);
        }, activity.getApplicationContext());
    }

    private void searchHero() {
        HeroesApi.getHeroes((int status, List<Hero> heroes) -> updateHeroesList(heroes), getContext(), search, actualAbility);
    }

    private void setupSearch() {
        mSpinner.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);
        goBackButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
    }

    private void cancelSearch() {
        mSpinner.setVisibility(View.GONE);
        mainContent.setVisibility(View.VISIBLE);
        search = null;
        actualAbility = null;
        exitButton.setVisibility(View.VISIBLE);
        goBackButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        mainScroll.setScrollY(0);
        searchHero();
    }

    private void setupAbilitySelector(MainActivity activity) {
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
        mainScroll = activity.mainScroll;
        activity.mainScroll.getViewTreeObserver().addOnScrollChangedListener(() -> {
            mainScroll.getLocationOnScreen(location);
            activity.heroesList.getLocationOnScreen(listLocation);
            if(listLocation[1] <= location[1] + 72) {
                setupSearch();
            }
        });

        AbilitiesApi.getAbilities((int status, List<String> list) -> {
            list.add(0, "Habilidade");
            abilities = list;
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
        exitButton = requireActivity().findViewById(R.id.logoutBtn);

        exitButton.setOnClickListener(view1 -> {
            CredentialsManager.logout(getActivity());
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
            requireActivity().finish();
        });
    }

    public void onEmit(Editable key) {
        search = key.toString();
        searchHero();
    }
}