package br.ufpr.heroes.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufpr.heroes.databinding.FragmentHeroCardBinding;
import br.ufpr.heroes.models.Hero;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Hero}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HeroCardRecyclerViewAdapter extends RecyclerView.Adapter<HeroCardRecyclerViewAdapter.ViewHolder> {

    private final List<Hero> mValues;

    public HeroCardRecyclerViewAdapter(List<Hero> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentHeroCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.heroName.setText(mValues.get(position).name);
        holder.movie.setText(mValues.get(position).movie);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView heroName;
        public final TextView movie;
        public final ImageView imageView;
        public Hero mItem;

        public ViewHolder(FragmentHeroCardBinding binding) {
            super(binding.getRoot());
            heroName = binding.searchCardHeroName;
            movie = binding.searchCardMovie;
            imageView = binding.cardHeroImage;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + movie.getText() + "'";
        }
    }
}