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
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnCardViewItemClick{
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private MoviesRepository mMoviesRepository;
    private Toolbar mToolbar;
    private SharedPreferences mPreference;
    private Integer mSortMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.view_movies);
        mMoviesRepository = MoviesRepository.getInstance();
        mToolbar = findViewById(R.id.toolbar);
        mPreference = this.getSharedPreferences("popular_movie",MODE_PRIVATE);
        mSortMode = mPreference.getInt("SortMode",0);
        setSupportActionBar(mToolbar);

        getMoviesToListView();
    }
    private void getMoviesToListView(){
        final MainActivity self = this;
        String sort = mSortMode==0?"popular":"top_rated";
        mMoviesRepository.getMovies(new TMDbGetMoviesCallback() {
            @Override
            public void onSuccess(List<RecyclerModel> movies) {
                Log.i("movie","success."+movies.toString());
                mMoviesAdapter = new MoviesAdapter(movies,self,self);
                mRecyclerView.setAdapter(mMoviesAdapter);
            }
            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        },sort);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String EXTRA_ID_FROM_LIST="EXTRA_ID_FROM_LIST";
    @Override
    public void onClick(Long id,Integer position) {
        Log.i("tag","clicked "+position+","+id);
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(EXTRA_ID_FROM_LIST,id.longValue());
        startActivity(intent);
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
        }
        if(prevMode != mSortMode) {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt("SortMode", mSortMode);
            editor.commit();
            getMoviesToListView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
