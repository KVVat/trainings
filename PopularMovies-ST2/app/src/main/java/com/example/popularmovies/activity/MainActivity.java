package com.example.popularmovies.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.popularmovies.R;
import com.example.popularmovies.constants.Constants;
import com.example.popularmovies.constants.MovieSortMode;
import com.example.popularmovies.databinding.ActivityMainBinding;
import com.example.popularmovies.datasource.MovieDataSourceFactory;
import com.example.popularmovies.utils.NetworkUtil;
import com.example.popularmovies.utils.SharedPreferenceUtil;
import com.example.popularmovies.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

/**
 * MainActivity
 *
 *  In this activity, I implement a RecyclerView that shows movie poster images.
 *  When user click a poster, User could load detail page correspond with each poster.
 *
 */
public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private MainViewModel viewModel;
    private MovieSortMode mSortMode;
    /**Did favorite data update from Detail activity? */
    private Boolean isFavoriteDirty = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mSortMode = MovieSortMode.valueOf(
                SharedPreferenceUtil.getInstance(this).getSortMode());

        MovieDataSourceFactory.setSort(mSortMode);

//        Intent intent = new Intent(this,DetailActivity.class);
//        intent.putExtra(Constants.INTENT_EXTRA_MOVIE_ID,24L);
//        startActivity(intent);

        setupBindings(savedInstanceState);


        BottomNavigationView b_nav = findViewById(R.id.bottom_navigation);
        b_nav.setOnNavigationItemSelectedListener(this);
        /*
         * We need to place these methods in here
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupMenuTitle(mSortMode);

        /*
         * append bottom menu
         */
        this.onNavigationItemSelected(mSortMode.getMenuRes());
        b_nav.setSelectedItemId(mSortMode.getMenuRes());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSortMode == MovieSortMode.SORT_MODE_FAVORITE && isFavoriteDirty){
            if(viewModel.moviePagedList.getValue() != null ) {
                viewModel.moviePagedList.getValue().getDataSource().invalidate();
            }
        }
    }

    private void setupBindings(Bundle savedInstanceState) {
        ActivityMainBinding activityBinding =
                DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        if (savedInstanceState == null) {
            viewModel.init();
            viewModel.getAdapter().setOnItemClickListener(view->{
                Long id = (Long)view.getTag(R.string.card_movie_id);

                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.INTENT_EXTRA_MOVIE_ID,id);
                startActivityForResult(intent,Constants.DETAIL_RESPONSE_CODE);
            });
        }
        activityBinding.setViewModel(viewModel);
        setupListUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.DETAIL_RESPONSE_CODE
                && resultCode == Activity.RESULT_OK){
            isFavoriteDirty =
                    data.getBooleanExtra("FAVORITE_IS_DIRTY", false);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private void setupListUpdate() {

        viewModel.loading.set(View.VISIBLE);

        final RecyclerView rv = findViewById(R.id.list_movies);
        rv.setAdapter(viewModel.getAdapter());
        rv.setNestedScrollingEnabled(true);

        viewModel.moviePagedList.observe(this,items->{
            viewModel.loading.set(View.GONE);
            if(viewModel.getAdapter()!=null) {
                viewModel.getAdapter().submitList(items);
                if (!NetworkUtil.isOnline() && mSortMode !=MovieSortMode.SORT_MODE_FAVORITE) {
                    viewModel.showEmpty.set(View.VISIBLE);
                } else {
                    viewModel.showEmpty.set(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    public void setupMenuTitle(MovieSortMode sortMode){
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.setTitle(sortMode.getTitleRes());
        }
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        return this.onNavigationItemSelected(item.getItemId());
    }
    public boolean onNavigationItemSelected(int menuItemId){//@NonNull MenuItem item) {
        MovieSortMode prevMode = mSortMode;
        switch (menuItemId) {
            case R.id.action_sort_popularity:
                mSortMode=MovieSortMode.SORT_MODE_POPULAR;
                break;
            case R.id.action_sort_toprated:
                mSortMode=MovieSortMode.SORT_MODE_TOPRATED;
                break;
            case R.id.action_items_favorite:
                mSortMode=MovieSortMode.SORT_MODE_FAVORITE;
                break;
        }
        if(prevMode != mSortMode) {
            viewModel.loading.set(View.VISIBLE);
            SharedPreferenceUtil.getInstance(this).setSortMode(mSortMode.getId());
            MovieDataSourceFactory.setSort(mSortMode);
            viewModel.getMovieDatasource().invalidate();
            setupMenuTitle(mSortMode);
            return true;
        }
        return true;
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
