package com.example.sayyaf.popularmovies.Movie;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.sayyaf.popularmovies.QueryUtils;

import java.util.List;

/*
 * This class was  based on the code and practices found in the udacity course android networking in the basic Android Nanodegree
 */
/*
Please note that alot  of the code here in this project is written based on the code and practices that I learned in the Android Basic: Networking course and Data Stroage
I applied the same design pattern and practices in this project.
*/
public class MovieAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String LOG_TAG = MovieAsyncTaskLoader.class.getName();
    private String mUrl;

    public MovieAsyncTaskLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Movie> movies = QueryUtils.fetchMoviesData(mUrl);
        return movies;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
