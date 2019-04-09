package com.ishdemon.tmdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.ishdemon.tmdbapp.databinding.ActivityMainBinding;
import com.ishdemon.tmdbapp.model.Result;
import com.ishdemon.tmdbapp.utils.ItemOffsetDecoration;
import com.ishdemon.tmdbapp.viewmodel.MainViewModel;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import java.util.Observable;
import java.util.Observer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements Observer {

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private ActivityMainBinding activityMainBinding;
    private MainViewModel mainViewModel;
    private FastItemAdapter<Result> mMovieAdapter;
    private ItemAdapter mFooterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        setupObserver(mainViewModel);
    }

    private void initDataBinding() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new MainViewModel(this);
        activityMainBinding.setViewModel(mainViewModel);
        mainViewModel.initializeViews();
    }

    public void setupObserver(Observable observable) {
        observable.addObserver(this);
    }

    private OnClickListener<Result> onClickListener = new OnClickListener<Result>() {
        @Override
        public boolean onClick(View v, IAdapter<Result> adapter, Result item, int position) {
            final Intent i = new Intent(MainActivity.this, DetailsActivity.class);
            i.putExtra("id", item.getId());
            i.putExtra("title", item.getTitle());
            startActivity(i);
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    private void setupRecyclerview(final RecyclerView recyclerview) {

        mMovieAdapter = new FastItemAdapter<>();
        mFooterAdapter = new ItemAdapter();
        mMovieAdapter.setHasStableIds(true);
        mMovieAdapter.addAdapter(1, mFooterAdapter);
        mMovieAdapter.withOnClickListener(onClickListener);
        recyclerview.setAdapter(mMovieAdapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setItemViewCacheSize(30);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mFooterAdapter) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.wtf("page", String.valueOf(currentPage + 1));
                mFooterAdapter.clear();
                mFooterAdapter.add(new ProgressItem().withEnabled(false));
                mainViewModel.scrollLoad(currentPage + 1);
            }
        };
        recyclerview.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainViewModel.reset();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof MainViewModel) {
            MainViewModel viewModel = (MainViewModel) observable;
            if ((boolean) o) {
                setupRecyclerview(activityMainBinding.listMovies);
            }
            mMovieAdapter.add(viewModel.getMovies());
        }
    }
}
