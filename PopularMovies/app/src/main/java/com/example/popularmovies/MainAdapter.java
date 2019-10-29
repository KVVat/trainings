package com.example.popularmovies;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.GenericViewHolder> {
    private int layoutId;
    private List<RecyclerModel> movies;
    private MainViewModel viewModel;
    public MainAdapter(@LayoutRes int layoutId, MainViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }
    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }
    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }
    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.i("TAG",""+viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate
                (layoutInflater, viewType, parent, false);

        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }
    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }
    public void setRecyclerModel(List<RecyclerModel> movies) {
        this.movies = movies;
    }
    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MainViewModel viewModel, Integer position) {
            //viewModel.fetchList();
            //viewModel.fetchMovieImagesAt(position);
            viewModel.fetchMovieImagesAt(position);
            binding.setVariable(BR.viewModel, viewModel);
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
        }

    }
}
