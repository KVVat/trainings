package com.example.popularmovies.apdater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.popularmovies.BR;
import com.example.popularmovies.R;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.viewmodel.DetailViewModel;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.GenericViewHolder> {
    private int layoutId;
    private DetailViewModel viewModel;
    public TrailerAdapter(@LayoutRes int layoutId, DetailViewModel viewModel) {
        Log.i("Observer","TrailerAdapter const");
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }
    @Override
    public int getItemCount() {
        if(this.viewModel!= null){
            return
                this.viewModel.mutableDetail.getValue().getTrailers().getResults().size();
        } else {
            return 0;
        }
    }

    //For to handle itemclick
    private View.OnClickListener listener;
    public void setOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.i("Observer","OnCreate Viwe Holder"+viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate
                (layoutInflater, viewType, parent, false);

        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
        holder.rootView.setOnClickListener(view->{ listener.onClick(view); });
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
        final View rootView;
        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.rootView = binding.getRoot();
        }

        void bind(DetailViewModel viewModel, int position) {

            //viewModel.fetchMovieImagesAt(position);

            binding.setVariable(BR.viewModel, viewModel);
            binding.setVariable(BR.posTrailer, position);

            Trailer trailer= viewModel.mutableDetail.getValue().getTrailers().getResults().get(position);
            rootView.setTag(R.string.trailer_url,trailer.getKey());

            binding.setVariable(BR.trailer,trailer);
            binding.executePendingBindings();
        }

    }
}
