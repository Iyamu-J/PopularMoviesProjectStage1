package com.ayevbeosa.popularmoviesprojectstage1.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayevbeosa.popularmoviesprojectstage1.R;
import com.ayevbeosa.popularmoviesprojectstage1.model.Movie;
import com.ayevbeosa.popularmoviesprojectstage1.model.MoviesResponse;
import com.ayevbeosa.popularmoviesprojectstage1.rest.ApiClient;
import com.ayevbeosa.popularmoviesprojectstage1.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ayevbeosa.popularmoviesprojectstage1.activity.MainActivity.API_KEY;
import static com.ayevbeosa.popularmoviesprojectstage1.adapter.MovieRecyclerViewAdapter.IMAGE_BASE_URL;
import static com.ayevbeosa.popularmoviesprojectstage1.adapter.MovieRecyclerViewAdapter.IMAGE_SIZE;

public class DetailsActivity extends AppCompatActivity {

    public static final int DEFAULT_POSITION = -1;
    private List<Movie> movies;

    @BindView(R.id.movie_poster_iv)
    ImageView moviePoster;
    @BindView(R.id.original_title_tv)
    TextView originalTitleTextView;
    @BindView(R.id.original_title_label)
    TextView originalTitleLabel;
    @BindView(R.id.overview_tv)
    TextView overviewTextView;
    @BindView(R.id.overview_label)
    TextView overviewLabel;
    @BindView(R.id.release_date_tv)
    TextView releaseDateTextView;
    @BindView(R.id.release_date_label)
    TextView releaseDateLabel;
    @BindView(R.id.vote_average_tv)
    TextView voteAverageTextView;
    @BindView(R.id.vote_average_label)
    TextView voteAverageLabel;
    @BindView(R.id.details_pb)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        loadProgressBar(true);
        final Bundle extras = getIntent().getExtras();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                loadProgressBar(false);
                movies = response.body().getResults();
                int position = extras.getInt(getString(R.string.extra_position));
                populateUI(position);
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
               loadProgressBar(false);
                closeOnError();
            }
        });
    }

    private void populateUI(int position) {
        if (position == DEFAULT_POSITION) {
            closeOnError();
            return;
        }

        String posterPath = movies.get(position).getPosterPath();
        String imagePath = IMAGE_BASE_URL + IMAGE_SIZE + posterPath;

        Picasso.with(this)
                .load(imagePath)
                .error(R.drawable.default_image_view)
                .into(moviePoster);

        String title = movies.get(position).getTitle();
        String originalTitle = movies.get(position).getOriginalTitle();
        String overview = movies.get(position).getOverview();
        String releaseDate = movies.get(position).getReleaseDate();
        Double voteAverage = movies.get(position).getVoteAverage();
        String voteAverageString = Double.toString(voteAverage);

        setTitle(title);

        originalTitleTextView.setText(originalTitle);
        overviewTextView.setText(overview);
        releaseDateTextView.setText(releaseDate);
        voteAverageTextView.setText(voteAverageString);
    }

    private void closeOnError() {
        Toast.makeText(DetailsActivity.this, getString(R.string.detail_error_message), Toast.LENGTH_LONG)
                .show();
        finish();
    }

    private void loadProgressBar(boolean load) {
        if (load) {
            progressBar.setVisibility(View.VISIBLE);
            originalTitleLabel.setVisibility(View.GONE);
            overviewLabel.setVisibility(View.GONE);
            releaseDateLabel.setVisibility(View.GONE);
            voteAverageLabel.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            originalTitleLabel.setVisibility(View.VISIBLE);
            overviewLabel.setVisibility(View.VISIBLE);
            releaseDateLabel.setVisibility(View.VISIBLE);
            voteAverageLabel.setVisibility(View.VISIBLE);
        }
    }
}
