package com.example.popularmovies.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.popularmovies.R;
import com.example.popularmovies.constants.Constants;
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
    private Integer mSortMode;
    private RecyclerView mRecyclerView;
    private Parcelable listState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.list_movies);
        Log.i("Observe","saveState:"+savedInstanceState);
        if(savedInstanceState != null) {
            listState = savedInstanceState.getParcelable("ListState");

        }
        mSortMode = SharedPreferenceUtil.getInstance(this).getSortMode();
        MovieDataSourceFactory.setSort(sortKey(mSortMode));

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
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.INTENT_EXTRA_MOVIE_ID,id);
                startActivity(intent);
            });
        }
        activityBinding.setViewModel(viewModel);
        setupListUpdate();
    }

    private void setupListUpdate() {

        viewModel.loading.set(View.VISIBLE);

        final RecyclerView rv = findViewById(R.id.list_movies);
        rv.setAdapter(viewModel.getAdapter());
        rv.setNestedScrollingEnabled(true);

        viewModel.moviePagedList.observe(this,items->{
            Log.i("Observe","observe called"+viewModel.getAdapter().getItemCount());
            viewModel.loading.set(View.GONE);
            if(viewModel.getAdapter()!=null) {
                viewModel.getAdapter().submitList(items);
                if (!NetworkUtil.isOnline()) {
                    viewModel.showEmpty.set(View.VISIBLE);
                } else {
                    viewModel.showEmpty.set(View.GONE);
                }
                mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
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


    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("Observe","restoreState called"+savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("Observe","saveState called");
        outState.putParcelable("ListState", mRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
        //outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());

    }
}
