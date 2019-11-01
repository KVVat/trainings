package com.example.popularmovies.apdater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.viewmodel.DetailViewModel;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.GenericViewHolder> {
    private int layoutId;
    private List<Trailer> trailers;
    private DetailViewModel viewModel;
    public TrailerAdapter(@LayoutRes int layoutId, DetailViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }
    @Override
    public int getItemCount() {
        return trailers == null ? 0 : trailers.size();
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

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }
    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DetailViewModel viewModel, int position) {

            //viewModel.fetchMovieImagesAt(position);
            //binding.setVariable(BR.viewModel, viewModel);
            //binding.setVariable(BR.position, position);
            binding.executePendingBindings();
        }

    }
}
