package com.example.popularmovies.apdater;

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

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int layoutId;
    private DetailViewModel viewModel;

    private int layoutHeaderId=R.layout.trailer_header;

    public TrailerAdapter(@LayoutRes int layoutId, DetailViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }
    @Override
    public int getItemCount() {
        if(this.viewModel!= null){
            int size = this.viewModel.mutableDetail.getValue().getTrailers().getResults().size();
            if(size == 0)
                return 0;
            else
                return size+1;
        } else {
            return 0;
        }
    }

    //For to handle item click
    private View.OnClickListener listener;
    public void setOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.i("Observer","OnCreate Viwe Holder"+viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if(viewType == this.layoutId) {
            ViewDataBinding binding = DataBindingUtil.inflate
                    (layoutInflater, viewType, parent, false);
            return new GenericViewHolder(binding);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);
            return new HeaderViewHolder(view);

            //layoutInflater.inflate(parent.getContext());
            //return new HeaderViewHolder(parent.getRootView());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position >0) {
            GenericViewHolder gh = (GenericViewHolder)holder;
            gh.bind(viewModel, position - 1);
            gh.rootView.setOnClickListener(view -> {
                if (listener != null) listener.onClick(view);
            });
        }
    }

    private int getLayoutIdForPosition(int position) {
        if (position==0) return this.layoutHeaderId;
        else return this.layoutId;
    }
    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
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

            binding.setVariable(BR.viewModel, viewModel);
            binding.setVariable(BR.posTrailer, position);

            Trailer trailer= viewModel.mutableDetail.getValue().getTrailers().getResults().get(position);
            rootView.setTag(R.string.trailer_url,trailer.getKey());

            binding.setVariable(BR.trailer,trailer);
            binding.executePendingBindings();
        }

    }
}
