package com.example.popularmovies.apdater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.R;
import com.example.popularmovies.model.Movie;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PagingMoviesAdapter extends PagedListAdapter<Movie, PagingMoviesAdapter.MovieViewHolder> {

    public PagingMoviesAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_view, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // I can't use data-binding in here.(In that case Jetpack paging method didn't call.
        // It looks need to pass each Items to the viewholder to use, Jetpack paging.
        holder.bindTo(getItem(position), position);
        holder.cardView.setOnClickListener(view->{ listener.onClick(view); });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    //For to handle itemclick
    private View.OnClickListener listener;
    public void setOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(Movie oldItem, Movie newItem) {

                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(Movie oldItem, Movie newItem) {

                    return oldItem.equals(newItem);
                }
            };


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        final CardView cardView;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imageView = itemView.findViewById(R.id.poster_view);
        }

        public void bindTo(Movie item, int position) {
            //text.setText("Page: " + item.id + " | ID:" + position);
            cardView.setTag(R.string.card_movie_id,item.getId());
            cardView.setTag(R.string.card_position_id,position);

            imageView.setImageBitmap(null);
            imageView.setTag(R.id.poster_view, item.getPosterPath());
            //
            Glide.with(imageView).load(BuildConfig.TMDbIMGPATH + item.getPosterPath())
                    .into(imageView);
        }
    }
}
