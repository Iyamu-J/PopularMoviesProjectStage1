package com.ayevbeosa.popularmoviesprojectstage1.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // the basic URL of the API
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    // an instance of the Retrofit Object
    private static Retrofit retrofit;

    /**
     * This method connects to the API and
     * retrieves the JSON response
     * @return a {@link #retrofit} instance
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
