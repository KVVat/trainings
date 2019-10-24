package com.example.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    public Long mMovieId;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        mMovieId = intent.getLongExtra(MainActivity.EXTRA_ID_FROM_LIST,0);
        if(mMovieId != null && mMovieId !=0){
            Log.i("TAG","call movie"+mMovieId);
            mTextView = (TextView)findViewById(R.id.detailTitle);
            mTextView.setText(""+mMovieId.toString());
        }
        mToolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
