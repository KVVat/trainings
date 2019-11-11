package com.example.popularmovies.apdater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.R;
import com.example.popularmovies.model.Movie;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A RecyclerView Adapter implements PagedListAdapter
 */
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
        // It looks need to pass each Items to the view holder for use Jetpack paging.
        Movie movie = getItem(position);
        if(movie != null) {
            holder.bindTo(movie, position);
        } else {
            holder.bindTo(new Movie("(Load failure)",0),position);
        }
        holder.cardView.setOnClickListener(view->listener.onClick(view));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    //To handle item click
    private View.OnClickListener listener;
    public void setOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(Movie oldItem, Movie newItem) {

                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(Movie oldItem, @NonNull Movie newItem) {

                    return oldItem.equals(newItem);
                }
            };


    class MovieViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final TextView textView;
        final CardView cardView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imageView = itemView.findViewById(R.id.poster_view);
            textView = itemView.findViewById(R.id.text_view);
        }

        void bindTo(Movie item, int position) {

            cardView.setTag(R.string.card_movie_id,item.getId());
            cardView.setTag(R.string.card_position_id,position);

            imageView.setImageBitmap(null);
            textView.setText(null);
            imageView.setTag(R.id.poster_view, item.getPosterPath());

            //Some Content has no poster path.
            String posterPath = item.getPosterPath();
            if(posterPath != null) {
                Glide.with(imageView).load(BuildConfig.TMDbIMGPATH + item.getPosterPath())
                        .into(imageView);
            } else {
                //In the case, I'll show the title in text.
                textView.setText(item.getTitle());
            }
        }
    }
}
