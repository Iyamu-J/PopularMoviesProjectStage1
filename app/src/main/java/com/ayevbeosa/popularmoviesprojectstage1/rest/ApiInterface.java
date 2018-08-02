package com.ayevbeosa.popularmoviesprojectstage1.rest;

import com.ayevbeosa.popularmoviesprojectstage1.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This interface specifies the endpoints and uses
 * annotations to specify the parameters and request methods
 */
public interface ApiInterface {

    /**
     * This method retrieves the movies according to its popularity
     * @param apiKey the required key for connecting to the API
     * @return {@link Call}
     */
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    /**
     * This method retrieves the movies according to its rating
     * @param apiKey the required key for connecting to the API
     * @return {@link Call}
     */
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);
}
