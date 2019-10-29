package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesAdapter extends RecyclerView.Adapter {
    interface OnCardViewItemClick{
        void onClick(Long id, Integer position);
    }
    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private List<RecyclerModel> dataset;
    private OnCardViewItemClick mCardViewClickListener;


    MoviesAdapter(List<RecyclerModel> myDataset, Context context, OnCardViewItemClick listener) {
        this.dataset = myDataset;
        this.mCardViewClickListener = listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        CardView mCardView;
        TextView mTextView;
        ImageView mPosterView;
        ViewHolder(View v) {
            super(v);
            //mCardView = (CardView)v.findViewById(R.id.card_view);
            //mTextView = (TextView)v.findViewById(R.id.text_view);
            mPosterView =(ImageView)v.findViewById(R.id.poster_view);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_view, parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).mTextView.setText(dataset.get(position).getTitle());
        //Log.i("bindimages",dataset.get(position).getPosterPath());
        Glide.with(ApplicationContext.instance)
                .load(IMAGE_BASE_URL + dataset.get(position).getPosterPath())
                .into(((ViewHolder)holder).mPosterView);
        final int finalPos = position;
        final Long movieId = dataset.get(position).getId();
        ((ViewHolder)holder).mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCardViewClickListener != null){
                    mCardViewClickListener.onClick(movieId,finalPos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
