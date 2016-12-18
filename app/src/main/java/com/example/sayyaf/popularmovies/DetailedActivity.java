package com.example.sayyaf.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sayyaf.popularmovies.Data.MovieContract.MovieEntry;
import com.example.sayyaf.popularmovies.Movie.Movie;
import com.example.sayyaf.popularmovies.Review.Review;
import com.example.sayyaf.popularmovies.Review.ReviewAdapter;
import com.example.sayyaf.popularmovies.Trailer.Trailer;
import com.example.sayyaf.popularmovies.Trailer.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TextView titleTextView;
    private TextView synopsisTextView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private ImageView thumbnailTextView;
    private ListView trailersListView;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private List<Trailer> trailers;
    private ListView reviewsListView;
    private List<Review> reviews;
    private Movie movie;
    private Button favorite;
    private TextView trailersLabel;
    private TextView reviewsLabel;
    private String orderBy;
    private View firstDivider;
    private View secondDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        viewsInitialization();
        movie = getIntent().getParcelableExtra("Movie");

        trailers = movie.getTrailers();
        reviews = movie.getReviews();

        trailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        reviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());

        viewsSettingValues(movie);


    }

    private void viewsSettingValues(Movie movie) {
        String title = movie.getTitle();
        String synopsis = movie.getSynopsis();
        String releaseDate = movie.getReleaseDate();
        releaseDate = releaseDate.substring(0, releaseDate.indexOf("-"));
        String rating = String.valueOf(movie.getRating()) + "/10";
        titleTextView.setText(title);
        synopsisTextView.setText(synopsis);
        releaseDateTextView.setText(releaseDate);
        ratingTextView.setText(rating);
        Picasso.with(this).load(movie.getImagethumbnail()).into(thumbnailTextView);
        if (orderBy.equals("popular") || orderBy.equals("top_rated")) {
            trailerAdapter.addAll(trailers);
            reviewAdapter.addAll(reviews);
            trailersListView.setAdapter(trailerAdapter);
            reviewsListView.setAdapter(reviewAdapter);
            trailersListView.setOnItemClickListener(this);
        } else {
            trailersListView.setVisibility(View.GONE);
            reviewsListView.setVisibility(View.GONE);
            favorite.setVisibility(View.GONE);
            trailersLabel.setVisibility(View.GONE);
            reviewsLabel.setVisibility(View.GONE);
            firstDivider.setVisibility(View.GONE);
            secondDivider.setVisibility(View.GONE);

        }

    }

    private void viewsInitialization() {
        trailersListView = (ListView) findViewById(R.id.trailers_listView);
        favorite = (Button) findViewById(R.id.markFavorite);
        reviewsListView = (ListView) findViewById(R.id.reviewsListView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        synopsisTextView = (TextView) findViewById(R.id.synopsis);
        releaseDateTextView = (TextView) findViewById(R.id.releaseDate);
        ratingTextView = (TextView) findViewById(R.id.rating);
        thumbnailTextView = (ImageView) findViewById(R.id.detaliedImageThumbnail);
        trailersLabel = (TextView) findViewById(R.id.trailersLabel);
        reviewsLabel = (TextView) findViewById(R.id.reviewsLabel);
        firstDivider=findViewById(R.id.firstDivider);
        secondDivider=findViewById(R.id.secondDivider);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String url = trailers.get(i).getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ((orderBy.equals("popular") || orderBy.equals("top_rated") && movie.getTrailers().size() > 0)) {
            getMenuInflater().inflate(R.menu.detial_menu, menu);
            MenuItem menuItem = menu.findItem(R.id.action_share);
            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if (shareActionProvider != null) {
                shareActionProvider.setShareIntent(shareIntent());
            }

            return true;
        } else {
            return false;
        }

    }

    public Intent shareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movie.getTrailers().get(0).getUrl()
        );
        return shareIntent;

    }

    public void markFavorite(View view) {
        String title = movie.getTitle();
        String synopsis = movie.getSynopsis();
        String releaseDate = movie.getReleaseDate();
        double rating = movie.getRating();
        String poster = movie.getImagethumbnail();
        int id = movie.getId();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry._ID, id);
        contentValues.put(MovieEntry.COLUMN_MOVIE_TITLE, title);

        contentValues.put(MovieEntry.COLUMN_MOVIE_SYNOPSIS, synopsis);
        contentValues.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
        contentValues.put(MovieEntry.COLUMN_MOVIE_POSTER, poster);


        contentValues.put(MovieEntry.COLUMN_MOVIE_USER_RATING, rating);

        Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
        if (ContentUris.parseId(uri) == -1) {
            Toast.makeText(this, "Movie has not been saved", Toast.LENGTH_SHORT).show();

        } else {
            if (ContentUris.parseId(uri) == -3) {
                Toast.makeText(this, "Movie is already saved as favorite", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Movie has been saved", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
