package com.ishdemon.tmdbapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ishdemon.tmdbapp.databinding.ActivityDetailsBinding;
import com.ishdemon.tmdbapp.viewmodel.DetailViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding activityDetailsBinding;
    private DetailViewModel detailViewModel;
    private int id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getIntExtra("id",0);
        title = getIntent().getStringExtra("title");
        initDataBinding(id);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDataBinding(int id) {
        activityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        detailViewModel = new DetailViewModel(id, this);
        activityDetailsBinding.setViewModel(detailViewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailViewModel.reset();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
