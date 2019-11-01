package com.example.popularmovies.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmovies.constants.Constants;
import com.example.popularmovies.databinding.ActivityDetailBinding;
import com.example.popularmovies.R;

import com.example.popularmovies.viewmodel.DetailViewModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class DetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    DetailViewModel viewModel;

    private ActivityDetailBinding mBinding;

    private Long movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        movieId = intent.getLongExtra(Constants.INTENT_EXTRA_MOVIE_ID,0);


        setupBindings(savedInstanceState);

        mToolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupBindings(Bundle savedInstanceState) {
        mBinding =
                DataBindingUtil.setContentView(this,R.layout.activity_detail);
        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        mBinding.setModel(viewModel);
        updateDetail();
    }

    private void updateDetail(){
        if(movieId != null && movieId !=0) {
            viewModel.getDetail(movieId).observe(this, detail -> {
                mBinding.setDetail(detail);
                mBinding.setLifecycleOwner(this);
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("OPTION","onCreateOption menu called.");
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("OPTION","PREPARE");
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("OPTION","SELECTED");
        switch (id){
            case R.id.action_reload_detail:
                if(movieId==null) movieId=24L;
                updateDetail();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
