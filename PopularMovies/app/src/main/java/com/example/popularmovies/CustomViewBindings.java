package com.example.popularmovies;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewBindings {
    @BindingAdapter("setAdapter")
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("imageUrl")
    public static void bindRecyclerViewAdapter(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            // If we don't do this, you'll see the old image appear briefly
            // before it's replaced with the current image
            if (imageView.getTag(R.id.poster_view) == null || !imageView.getTag(R.id.poster_view).equals(imageUrl)) {
                imageView.setImageBitmap(null);
                imageView.setTag(R.id.poster_view, imageUrl);
                Glide.with(imageView).load(BuildConfig.IMDbIMGPATH+imageUrl)
                        //.override(Target.SIZE_ORIGINAL)
                        .into(imageView);
            }
        } else {
            imageView.setTag(R.id.poster_view, null);
            imageView.setImageBitmap(null);
        }
    }
}
