package br.ufpr.ws_mutantes.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import br.ufpr.ws_mutantes.api.RequestClient;
import br.ufpr.ws_mutantes.databinding.FragmentHeroCardBinding;
import br.ufpr.ws_mutantes.fragments.SearchFragment;
import br.ufpr.ws_mutantes.models.Hero;
import br.ufpr.ws_mutantes.views.MutantActivity;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView heroName;
        public final TextView movie;
        public final ImageView imageView;
        public Hero mItem;

        public ViewHolder(FragmentHeroCardBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            heroName = binding.mutantName;
            movie = binding.mutantMovie;
            imageView = binding.mutantImage;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + movie.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            int mutantPosition = getBindingAdapterPosition();
            Intent mutantIntent = new Intent(v.getContext(), MutantActivity.class);
            mutantIntent.putExtra("mutantId", SearchFragment.heroes.get(mutantPosition).id);
            v.getContext().startActivity(mutantIntent);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}