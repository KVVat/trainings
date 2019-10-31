package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmovies.databinding.ActivityMainBinding;
import com.example.popularmovies.datasource.MovieDataSourceFactory;
import com.example.popularmovies.model.Movie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    private Toolbar mToolbar;
    private SharedPreferences mPreference;
    private Integer mSortMode;
    private MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.view_movies);


        mPreference = this.getSharedPreferences("popular_movie",MODE_PRIVATE);
        mSortMode = mPreference.getInt("SortMode",0);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// 4.3で設定したhomeボタンを押下できる設定にする。
        //getSupportActionBar().setHomeButtonEnabled(true);

        ///getMoviesToListView();
        setupBindings(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupBindings(Bundle savedInstanceState) {
        ActivityMainBinding activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (savedInstanceState == null) {
            viewModel.init(this);
        }
        activityBinding.setModel(viewModel);
        //Log.i("aaa","startbinding");
        setupListUpdate();
    }
    private void setupListUpdate() {
       // Log.i("Changed","setupListUpdate called");
        //viewModel.loading.set(View.VISIBLE);
        //final PagingMoviesAdapter adapter = new PagingMoviesAdapter(this);
        final RecyclerView rv = (RecyclerView)findViewById(R.id.view_movies);
        rv.setAdapter(viewModel.getAdapter2());
        rv.setNestedScrollingEnabled(true);

        viewModel.moviePagedList.observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(@Nullable PagedList<Movie> items) {
                viewModel.getAdapter2().submitList(items);
            }
        });
        /*
        viewModel.fetchList();
        viewModel.getMutableMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                //Log.i("Changed","chanegd");
                if (movies.size() == 0) {
                    //viewModel.showEmpty.set(View.VISIBLE);
                } else {
                    //viewModel.showEmpty.set(View.GONE);
                    viewModel.setResponseModelInAdapter(movies);
                }
            }

        });*/
        setupListClick();
    }
    private void setupListClick()
    {

    }
    /*
    private void getMoviesToListView(){
        final MainActivity self = this;
        String sort = mSortMode==0?"popular":"top_rated";
        mMoviesRepository.getMovies(new TMDbGetMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                Log.i("movie","success."+movies.toString());
                mMoviesAdapter = new MoviesAdapter(movies,self,self);
                mRecyclerView.setAdapter(mMoviesAdapter);
            }
            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        },sort);
    }*/

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String EXTRA_ID_FROM_LIST="EXTRA_ID_FROM_LIST";
    //@Override
    public void onClick(Long id,Integer position) {
        Log.i("tag","clicked "+position+","+id);
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(EXTRA_ID_FROM_LIST,id.longValue());
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(mSortMode==0)
            mToolbar.setTitle(R.string.most_poular);
        else
            mToolbar.setTitle(R.string.top_rated);
        return true;//super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i("Observe","on prepare option called");
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
        Log.i("Observe","on opton select called");
        int id = item.getItemId();
        int prevMode = mSortMode;
        switch (id){
            case R.id.action_sort_popularity:
                mSortMode=0;
                mToolbar.setTitle(R.string.most_poular);
                break;
            case R.id.action_sort_toprated:
                mSortMode=1;
                mToolbar.setTitle(R.string.top_rated);
                break;
        }
        if(prevMode != mSortMode) {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt("SortMode", mSortMode);
            editor.commit();
            MovieDataSourceFactory.mSort = mSortMode==0?"popular":"top_rated";
            viewModel.moviePagedList.getValue().getDataSource().invalidate();
            //getMoviesToListView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
