package com.example.sayyaf.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sayyaf.popularmovies.Data.MovieContract;
import com.example.sayyaf.popularmovies.Data.MovieContract.MovieEntry;
import com.example.sayyaf.popularmovies.Movie.Movie;
import com.example.sayyaf.popularmovies.Movie.MovieArrayAdapter;
import com.example.sayyaf.popularmovies.Movie.MovieAsyncTaskLoader;
import com.example.sayyaf.popularmovies.Movie.MovieCursorAdapter;

import java.util.ArrayList;
import java.util.List;

/*
Please note that alot  of the code here in this project is written based on the code and practices that I learned in the Android Basic: Networking course and Data Stroage
I applied the same design pattern and practices in this project.
*/

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final String baseURL = "http://api.themoviedb.org/3/movie/";
    public static final String MovieDB_KEY = "";
    private GridView moviesGridView;
    private MovieArrayAdapter movieAdapter;
    private List<Movie> moviesArray;
    private ProgressBar progressBar;

    private TextView mEmptyStateTextView;
    private MovieCursorAdapter movieCursorAdapter;
    private Cursor cursor;
    private LoaderManager.LoaderCallbacks<List<Movie>> movieAsyncTaskLoader;
    private LoaderManager.LoaderCallbacks<Cursor> movieCursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesGridView = (GridView) findViewById(R.id.moviesGridView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        movieAdapter = new MovieArrayAdapter(this, new ArrayList<Movie>());
        moviesArray = new ArrayList<>();
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        moviesGridView.setOnItemClickListener(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        LoaderManager loaderManager = getLoaderManager();
        if (orderBy.equals("popular") || orderBy.equals("top_rated")) {

            //Use ArrayAdapter when fetching from the interent
            movieAsyncTaskLoader = new LoaderManager.LoaderCallbacks<List<Movie>>() {
                @Override
                public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
                    Uri baseUri = Uri.parse(baseURL);

                    Uri.Builder uriBuilder = baseUri.buildUpon();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
                    uriBuilder.appendPath(orderBy);

                    uriBuilder.appendQueryParameter("api_key", MovieDB_KEY);

                    return new MovieAsyncTaskLoader(getBaseContext(), uriBuilder.toString());
                }

                @Override
                public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
                     movieAdapter.clear();
                    if (movies != null && !movies.isEmpty()) {
                        movieAdapter.addAll(movies);
                        moviesArray = movies;
                        progressBar.setVisibility(View.INVISIBLE);



                    }
                }

                @Override
                public void onLoaderReset(Loader<List<Movie>> loader) {
                    movieAdapter.clear();
                }


            };

            moviesGridView.setAdapter(movieAdapter);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                loaderManager.initLoader(0, null, movieAsyncTaskLoader);
            } else {
                mEmptyStateTextView.setText(R.string.no_internet_connection);

            }
        } else {
            // if there is saved movies show them otherwise indicate there are no saved favorite movies
            movieCursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    return new CursorLoader(getBaseContext(), MovieEntry.CONTENT_URI, null, null, null, null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

                    movieCursorAdapter.swapCursor(cursor);
                    int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
                    int nameColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_TITLE);
                    int nameColumnPoster = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER);
                    int nameColumnReleaseDate = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
                    int nameColumnUserRating = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_USER_RATING);
                    int nameColumnsSynopsis = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_SYNOPSIS);

                    try {

                        while (cursor.moveToNext()) {


                            int currentID = cursor.getInt(idColumnIndex);
                            String currentTitle = cursor.getString(nameColumnIndex);
                            String currentPoster = cursor.getString(nameColumnPoster);
                            String currentReleaseDate = cursor.getString(nameColumnReleaseDate);
                            String currentUserRating = cursor.getString(nameColumnUserRating);
                            String currentSynopsis = cursor.getString(nameColumnsSynopsis);

                            moviesArray.add(new Movie(currentID, Double.parseDouble(currentUserRating), currentSynopsis, currentPoster, currentTitle, currentReleaseDate));
                        }

                    } finally {
                        if (!moviesArray.isEmpty()) {
                            movieCursorAdapter = new MovieCursorAdapter(getBaseContext(), cursor);

                            moviesGridView.setAdapter(movieCursorAdapter);


                        } else {
                            mEmptyStateTextView.setText(R.string.no_favorite_movie);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }


                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    movieCursorAdapter.swapCursor(null);
                }
            };
            movieCursorAdapter = new MovieCursorAdapter(this, null);
            moviesGridView.setAdapter(movieCursorAdapter);
            loaderManager.initLoader(1, null, movieCursorLoader);


        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("Movie", moviesArray.get(i));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
