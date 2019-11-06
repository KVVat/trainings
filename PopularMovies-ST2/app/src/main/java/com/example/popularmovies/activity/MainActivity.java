package com.example.popularmovies.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.Menu;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

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

        MovieDataSourceFactory.setSort(mSortMode);////sortKey(mSortMode));

        /*Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_MOVIE_ID,24L);
        startActivity(intent);
        */
        setupBindings(savedInstanceState);

        /*
         * We need to place setup Toolbar in here
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupMenuTitle(mSortMode);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //use data binding//
        menu.findItem(R.id.action_sort_popularity).setChecked(false);
        menu.findItem(R.id.action_sort_toprated).setChecked(false);
        menu.findItem(R.id.action_items_favorite).setChecked(false);
        if(mSortMode.getId() == 0) {
            menu.findItem(R.id.action_sort_popularity).setChecked(true);
        } else if(mSortMode.getId()==1){
            menu.findItem(R.id.action_sort_toprated).setChecked(true);
        } else if(mSortMode.getId() == 2){
            menu.findItem(R.id.action_items_favorite).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MovieSortMode prevMode = mSortMode;
        switch (id){
            case R.id.action_sort_popularity:
                mSortMode=MovieSortMode.SORT_MODE_POPULAR;
                break;
            case R.id.action_sort_toprated:
                mSortMode=MovieSortMode.SORT_MODE_TOPRATED;
                break;
            case R.id.action_items_favorite:
                mSortMode=MovieSortMode.SORT_MODE_FAVORITE;
                break;
            case R.id.action_reload_screen:
                viewModel.loading.set(View.VISIBLE);
                viewModel.getMovieDatasource().invalidate();
                return true;
        }
        if(prevMode != mSortMode) {
            viewModel.loading.set(View.VISIBLE);
            SharedPreferenceUtil.getInstance(this).setSortMode(mSortMode.getId());
            MovieDataSourceFactory.setSort(mSortMode);
            viewModel.getMovieDatasource().invalidate();
            setupMenuTitle(mSortMode);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    //private static final String BUNDLE_RECYCLER_STATE = "popularmoview.recycler.state";

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Log.i("Observe","restoreState called"+savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //Log.i("Observe","saveState called");
        //outState.putParcelable(BUNDLE_RECYCLER_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}
