package com.example.popularmovies.apdater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.popularmovies.BR;
import com.example.popularmovies.R;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.viewmodel.DetailViewModel;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView adapter that shows in DetailActivity to show reviews in each movies.
 *
 */
public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int layoutId;
    private DetailViewModel viewModel;
    public ReviewsAdapter(@LayoutRes int layoutId, DetailViewModel viewModel) {
        //Log.i("Observer","ReviewsAdapter const");
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }
    @Override
    public int getItemCount() {
        if(this.viewModel!= null){
           int size= 0;
           if(viewModel.mutableDetail.getValue() != null) {
               size = viewModel.mutableDetail.getValue().getReviews().getResults().size();
           }
           if(size == 0)
                return 0;
            else
                return size+1;
        } else {
            return 0;
        }
    }

    //For to handle itemclick
    //private View.OnClickListener listener;
    //public void setOnItemClickListener(View.OnClickListener listener) {
    //    this.listener = listener;
    //}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if(viewType == this.layoutId) {
            ViewDataBinding binding = DataBindingUtil.inflate
                    (layoutInflater, viewType, parent, false);
            return new GenericViewHolder(binding);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);
            return new HeaderViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position >0) {
            GenericViewHolder gh = (GenericViewHolder)holder;
            gh.bind(viewModel, position - 1);
            //gh.rootView.setOnClickListener(view -> {
            //    if (listener != null) listener.onClick(view);
            //});
        }
    }

    private int getLayoutIdForPosition(int position) {
        if (position==0) return R.layout.reviews_header;
        else return this.layoutId;
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }
    /**
     * ViewHolder for Header
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * ViewHolder for Each Line (Use Data binding to show data)
     */
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
            binding.setVariable(BR.posReview, position);

            if(viewModel.mutableDetail.getValue() != null) {
                Review review =
                        viewModel.mutableDetail.getValue().getReviews().getResults().get(position);

                binding.setVariable(BR.review, review);
            }
            binding.executePendingBindings();
        }

    }
}
