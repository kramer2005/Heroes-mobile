package br.ufpr.heroes.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import br.ufpr.heroes.api.RequestClient;
import br.ufpr.heroes.databinding.FragmentHeroCardBinding;
import br.ufpr.heroes.models.Hero;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Hero}.
 */
public class HeroCardRecyclerViewAdapter extends RecyclerView.Adapter<HeroCardRecyclerViewAdapter.ViewHolder> {

    Context ctx;

    public HeroCardRecyclerViewAdapter() {}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();

        return new ViewHolder(FragmentHeroCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = SearchFragment.heroes.get(position);
        holder.heroName.setText(SearchFragment.heroes.get(position).name);
        holder.movie.setText(SearchFragment.heroes.get(position).movie);
        Glide.with(ctx)
                .load(RequestClient.API_URL + "/images/" + SearchFragment.heroes.get(position).image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return SearchFragment.heroes.size();
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