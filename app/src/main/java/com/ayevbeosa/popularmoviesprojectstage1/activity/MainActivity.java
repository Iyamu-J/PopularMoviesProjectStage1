package com.ayevbeosa.popularmoviesprojectstage1.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ayevbeosa.popularmoviesprojectstage1.R;
import com.ayevbeosa.popularmoviesprojectstage1.adapter.MovieRecyclerViewAdapter;
import com.ayevbeosa.popularmoviesprojectstage1.model.Movie;
import com.ayevbeosa.popularmoviesprojectstage1.model.MoviesResponse;
import com.ayevbeosa.popularmoviesprojectstage1.rest.ApiClient;
import com.ayevbeosa.popularmoviesprojectstage1.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ListItemClickListener {

    public static final String API_KEY = "cbb90029f34e49e72f26a93e8d60fc54";

    private MovieRecyclerViewAdapter mAdapter;
    private List<Movie> movies;

    private boolean isRated;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        populateUI();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateUI();
            }
        });
    }

    private void populateUI() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> responseCall;
        if (isRated) {
            responseCall = apiService.getTopRatedMovies(API_KEY);
            loadProgressBar(true);
        } else {
            responseCall = apiService.getPopularMovies(API_KEY);
            loadProgressBar(true);
        }
        responseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                loadProgressBar(false);
                movies = response.body().getResults();
                mAdapter = new MovieRecyclerViewAdapter(MainActivity.this, movies, MainActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerView.setAdapter(mAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                loadProgressBar(false);
                Toast.makeText(MainActivity.this, getString(R.string.connection_error_message), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popular_action:
                isRated = false;
                populateUI();
                return true;
            case R.id.sort_by_rated_action:
                isRated = true;
                populateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(int clickItemIndex) {
        int id = mAdapter.getMovies().get(clickItemIndex).getId();
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, DetailsActivity.class);
        extras.putInt(getString(R.string.extra_position), clickItemIndex);
        extras.putInt(getString(R.string.extra_id), id);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void loadProgressBar(boolean load) {
        if (load) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
