package com.example.popularmovies.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.constants.Constants;
import com.example.popularmovies.databinding.ActivityMainBinding;

import com.example.popularmovies.datasource.MovieDataSourceFactory;
import com.example.popularmovies.utils.NetworkUtil;
import com.example.popularmovies.utils.SharedPreferenceUtil;
import com.example.popularmovies.viewmodel.MainViewModel;
import com.example.popularmovies.R;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private Integer mSortMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_main);

        mSortMode = SharedPreferenceUtil.getInstance(this).getSortMode();
        MovieDataSourceFactory.setSort(sortKey(mSortMode));
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_MOVIE_ID,24L);
        startActivity(intent);
        setupBindings(savedInstanceState);

        /*
         * We need to place setup Toolbar in here
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setupMenuTitle(mSortMode);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setupBindings(Bundle savedInstanceState) {
        ActivityMainBinding activityBinding =
                DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (savedInstanceState == null) {
            viewModel.init(this);
            viewModel.getAdapter().setOnItemClickListener(view->{
                Long id = (Long)view.getTag(R.string.card_movie_id);
                Intent intent = new Intent(this,DetailActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_MOVIE_ID,id);
                startActivity(intent);
            });
        }
        activityBinding.setViewModel(viewModel);
        setupListUpdate();
    }

    private void setupListUpdate() {

        //viewModel.loading.set(View.VISIBLE);

        final RecyclerView rv = findViewById(R.id.list_movies);
        rv.setAdapter(viewModel.getAdapter());
        rv.setNestedScrollingEnabled(true);

        viewModel.moviePagedList.observe(this,items->{
            //Log.i("Observe","observe called"+viewModel.getAdapter().getItemCount());
            viewModel.loading.set(View.GONE);
            if(viewModel.getAdapter()!=null) {
                viewModel.getAdapter().submitList(items);
                if (!NetworkUtil.isOnline()) {
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

        menu.findItem(R.id.action_sort_popularity).setChecked(false);
        menu.findItem(R.id.action_sort_toprated).setChecked(false);
        if(mSortMode == 0) {
            menu.findItem(R.id.action_sort_popularity).setChecked(true);
        } else {
            menu.findItem(R.id.action_sort_toprated).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int prevMode = mSortMode;
        switch (id){
            case R.id.action_sort_popularity:
                mSortMode=0;
                break;
            case R.id.action_sort_toprated:
                mSortMode=1;
                break;
            case R.id.action_reload_screen:
                viewModel.loading.set(View.VISIBLE);
                viewModel.moviePagedList.getValue().getDataSource().invalidate();
                return true;
        }
        if(prevMode != mSortMode) {
            viewModel.loading.set(View.VISIBLE);
            SharedPreferenceUtil.getInstance(this).setSortMode(mSortMode);
            MovieDataSourceFactory.setSort(sortKey(mSortMode));
            viewModel.moviePagedList.getValue().getDataSource().invalidate();
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

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void setupMenuTitle(int mSortMode){
        ActionBar toolbar = getSupportActionBar();
        switch (mSortMode) {
            case 0:
                toolbar.setTitle(R.string.most_popular);
                break;
            case 1:
                toolbar.setTitle(R.string.top_rated);
        }
    }
    public static String sortKey(int sortMode){
        if(sortMode==0){
            return "popular";
        } else {
            return "top_rated";
        }
    }
}
