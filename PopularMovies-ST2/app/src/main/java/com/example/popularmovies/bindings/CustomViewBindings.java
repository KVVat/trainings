package com.example.popularmovies.bindings;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.R;

import androidx.databinding.BindingAdapter;

public class CustomViewBindings {

    @BindingAdapter("imageUrl")
    public static void bindRecyclerViewAdapter(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            // If we don't do this, you'll see the old image appear briefly
            // before it's replaced with the current image
            if (imageView.getTag(R.id.poster_view) == null || !imageView.getTag(R.id.poster_view).equals(imageUrl)) {
                imageView.setImageBitmap(null);
                imageView.setTag(R.id.poster_view, imageUrl);
                Glide.with(imageView).load(BuildConfig.TMDbIMGPATH+imageUrl)
                        .into(imageView);
            }
        } else {
            imageView.setTag(R.id.poster_view, null);
            imageView.setImageBitmap(null);
        }
    }
}