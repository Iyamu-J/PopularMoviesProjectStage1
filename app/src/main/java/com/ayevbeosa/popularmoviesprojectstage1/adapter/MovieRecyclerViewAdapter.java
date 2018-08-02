package com.ayevbeosa.popularmoviesprojectstage1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayevbeosa.popularmoviesprojectstage1.R;
import com.ayevbeosa.popularmoviesprojectstage1.model.Movie;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class binds view data to the {@link RecyclerView}
 */
public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context mContext;

    private final ListItemClickListener mOnClickItemListener;

    public static final String IMAGE_SIZE = "w185/";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies, ListItemClickListener mOnClickItemListener) {
        this.mContext = context;
        this.movies = movies;
        this.mOnClickItemListener = mOnClickItemListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        String originalTitle = movies.get(position).getOriginalTitle();
        String posterPath = movies.get(position).getPosterPath();
        holder.bind(originalTitle, posterPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickItemIndex);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_movie_poster_iv) ImageView moviePoster;
        @BindView(R.id.item_original_title_tv) TextView originalTitle;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        private void bind(String title, String posterPath) {
            originalTitle.setText(title);
            String imagePath = IMAGE_BASE_URL + IMAGE_SIZE + posterPath;
            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.downloader(new OkHttp3Downloader(mContext));
            builder.build()
                    .load(imagePath)
                    .error(R.drawable.default_image_view)
                    .into(moviePoster);
        }
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickItemListener.onListItemClick(clickedPosition);
        }
    }
}
