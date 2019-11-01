package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.model.Movie;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PagingMoviesAdapter extends PagedListAdapter<Movie, PagingMoviesAdapter.MovieViewHolder> {
    private Context mCtx;
    private MainViewModel viewModel;
    PagingMoviesAdapter(Context mCtx, MainViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.viewModel=viewModel;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //Log.i("Observe","ocvholder");
        //View view = LayoutInflater.from(mCtx).inflate(R.layout.movie_view, parent, false);
        //LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //ViewDataBinding binding = DataBindingUtil.inflate
        //        (layoutInflater, R.layout.movie_view, parent, false);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_view, viewGroup, false);
        return new PagingMoviesAdapter.MovieViewHolder(view);

        //return new PagingMoviesAdapter.MovieViewHolder(binding);
        //return new GenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        //
        //Log.i("Observe","obvholder "+movie.getId());
        //onBindViewHolderSuper(viewHolder, position);
        //super.onBindViewHolder(holder,position);
        holder.bindTo(getItem(position), position);
    }
    @Override
    public int getItemCount() {
        return super.getItemCount() ;
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

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.poster_view);
        }

        public void bindTo(Movie item, int position) {
            //text.setText("Page: " + item.id + " | ID:" + position);
            imageView.setImageBitmap(null);
            imageView.setTag(R.id.poster_view, item.getPosterPath());
            Glide.with(imageView).load(BuildConfig.IMDbIMGPATH+item.getPosterPath())
                    //.override(Target.SIZE_ORIGINAL)
                    .into(imageView);
        }
    }

/*
    class MovieViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        MovieViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Movie movie, Integer position) {
            //viewModel.fetchList();
            //viewModel.fetchMovieImagesAt(position);
            //viewModel.fetchMovieImagesAt(position);
            binding.setVariable(BR.movie, movie);
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
            //binding.invalidateAll();
        }

    }
*/
}
