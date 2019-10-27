package com.example.popularmovies;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.popularmovies.model.Detail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    public Long mMovieId;
    private TextView mTextView;
    private TextView mOverviewView;
    private ImageView mPosterView;

    private MoviesRepository mMoviesRepository;
    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        mMovieId = intent.getLongExtra(MainActivity.EXTRA_ID_FROM_LIST,0);
        if(mMovieId != null && mMovieId !=0){
            Log.i("TAG","call movie"+mMovieId);
            mTextView = (TextView)findViewById(R.id.detailOriginalTitle);
            mOverviewView = (TextView)findViewById(R.id.detailOverview);
            mPosterView = (ImageView)findViewById(R.id.detailPosterView);
            mTextView.setText(""+mMovieId.toString());
            mMoviesRepository = MoviesRepository.getInstance();
            getDetail(mMovieId.toString());
        }
        mToolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    private void getDetail(String id){
        final DetailActivity self = this;
        mMoviesRepository.getDetail(new TMDbGetDetailCallback() {
            @Override
            public void onSuccess(Detail detail) {
                Log.i("movie","success."+detail.toString()+","+detail.getTitle()+","+detail.getOriginalTitle());
                mTextView.setText(detail.getOriginalTitle());
                mOverviewView.setText(detail.getOverview());
                Glide.with(ApplicationContext.instance)
                        .load(BuildConfig.IMDbIMGPATH +detail.getPosterPath())
                        .into(mPosterView);
            }
            @Override
            public void onError() {
                Toast.makeText(DetailActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        },id);
    }
}
