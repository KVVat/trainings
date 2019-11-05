package com.example.popularmovies.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

import com.example.popularmovies.R;
import com.example.popularmovies.constants.Constants;
import com.example.popularmovies.databinding.ActivityDetailBinding;
import com.example.popularmovies.model.Detail;
import com.example.popularmovies.model.Favorite;
import com.example.popularmovies.persistence.FavoriteRepository;
import com.example.popularmovies.utils.SharedPreferenceUtil;
import com.example.popularmovies.viewmodel.DetailViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    DetailViewModel viewModel;
    private ActivityDetailBinding mBinding;
    private Long movieId;
    private Boolean favorite_dirty=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        movieId = intent.getLongExtra(Constants.INTENT_EXTRA_MOVIE_ID,0);
        setupBindings(savedInstanceState);

        //Repair Checkbox Status Here//

        CheckBox checkBox = findViewById(R.id.checkFavorite);
        checkBox.setOnClickListener(v->{
            CheckBox chk = (CheckBox)v;
            Detail detail = viewModel.mutableDetail.getValue();
            if(chk.isChecked()){
                FavoriteRepository.getInstance().insert(new Favorite(
                        movieId.intValue(),true,detail.getTitle(),detail.getPosterPath()));
            } else {
                FavoriteRepository.getInstance().insert(new Favorite(
                        movieId.intValue(),false, detail.getTitle(),detail.getPosterPath()));
            }
            favorite_dirty=true;//mark dirty for parent update.
        });

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
        if(savedInstanceState == null){
            viewModel.init(this);
            viewModel.getTrailerAdapter().setOnItemClickListener(view->{
                String site = (String)view.getTag(R.string.trailer_url);
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v="+site));
                if(viewModel.isForceChrome.get()) {
                    webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    webIntent.setPackage("com.android.chrome");
                }
                Intent chooser = Intent.createChooser(webIntent,"Open url from");
                try {
                    this.startActivity(chooser);
                } catch (ActivityNotFoundException ex) { }
            });
        }
        mBinding.setModel(viewModel);
        updateDetail();
    }

    @Override
    public void onBackPressed() {
        Intent is_dirty = new Intent();
        is_dirty.putExtra("FAVORITE_IS_DIRTY", favorite_dirty);
        setResult(RESULT_OK, is_dirty);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("detail",viewModel.mutableDetail.getValue());

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        Detail detail = (savedInstanceState.getParcelable("detail"));
        if(detail != null) viewModel.mutableDetail.setValue(detail);

    }

    private void updateDetail(){
        if(movieId != null && movieId !=0) {

            final RecyclerView rvt= findViewById(R.id.list_trailers);
            rvt.setAdapter(viewModel.getTrailerAdapter());
            //Append Divider To Trailer View
            DividerItemDecoration dividerItemDecoration =
                    new DividerItemDecoration(rvt.getContext(),
                    new LinearLayoutManager(this).getOrientation());
            rvt.addItemDecoration(dividerItemDecoration);

            final RecyclerView rvr = findViewById(R.id.list_reviews);
            rvr.setAdapter(viewModel.getReviewsAdapter());
            //We don't need to reload data from api when the state is saved
            if(viewModel.mutableDetail.getValue()==null) {
                viewModel.getDetail(movieId).observe(this, detail -> {
                    mBinding.setDetail(detail);
                    mBinding.setLifecycleOwner(this);
                });
            } else {
                mBinding.setDetail(viewModel.mutableDetail.getValue());
                mBinding.setLifecycleOwner(this);
            }

            viewModel.getIsFavorite(movieId.intValue());
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_force_chrome).setChecked(viewModel.isForceChrome.get());
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
            case R.id.action_reload_detail:
                if(movieId==null) movieId=24L;
                updateDetail();
                break;
            case R.id.action_force_chrome:
                viewModel.toggleForceChrome();
                SharedPreferenceUtil.getInstance(this).setForceChorme(
                        viewModel.isForceChrome.get());
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
