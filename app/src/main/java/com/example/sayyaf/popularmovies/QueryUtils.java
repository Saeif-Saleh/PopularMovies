package com.example.sayyaf.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import com.example.sayyaf.popularmovies.Movie.Movie;
import com.example.sayyaf.popularmovies.Review.Review;
import com.example.sayyaf.popularmovies.Trailer.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/*
 * This class was  based on the code and practices found in the udacity course android networking in the basic Android Nanodegree
 */
public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Movie> fetchMoviesData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);

        }
        List<Movie> movies = extractFromJson(jsonResponse);
        return movies;
    }

    public static List<Trailer> fetchTrailerData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);

        }
        List<Trailer> trailers = extractFromJsonTrailer(jsonResponse);
        return trailers;
    }

    public static List<Review> fetchReviewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);

        }
        List<Review> reviews = extractFromJsonReview(jsonResponse);
        return reviews;
    }

    private static List<Review> extractFromJsonReview(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Review> reviews = new ArrayList<>();
        String content = "";
        String author = "";

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray reviewsarray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < reviewsarray.length(); i++) {
                JSONObject currentReview = reviewsarray.getJSONObject(i);
                author = currentReview.getString("author");
                content = currentReview.getString("content");


                reviews.add(new Review(content, author));


            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the trailer JSON results", e);
        }
        return reviews;
    }


    private static List<Trailer> extractFromJsonTrailer(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        final String baseUrl = "https://www.youtube.com/watch?v=";
        List<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray trailersArrays = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < trailersArrays.length(); i++) {
                JSONObject currentTrailer = trailersArrays.getJSONObject(i);

                String url = baseUrl + currentTrailer.getString("key");

                trailers.add(new Trailer(url));


            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the trailer JSON results", e);
        }
        return trailers;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movies JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Movie> extractFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        final String baseUrl = "http://image.tmdb.org/t/p/w185";
        List<Movie> movies = new ArrayList<>();
        List<Trailer> trailers;
        List<Review> reviews;
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray moviesArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject currentMovie = moviesArray.getJSONObject(i);

                String title = currentMovie.getString("original_title");
                String thumbnail = baseUrl + currentMovie.getString("poster_path");
                String synopsis = currentMovie.getString("overview");
                double rating = currentMovie.getDouble("vote_average");
                String releaseDate = currentMovie.getString("release_date");
                int id = currentMovie.getInt("id");
                trailers = fetchTrailerData("http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + MainActivity.MovieDB_KEY);
                reviews = fetchReviewsData("http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + MainActivity.MovieDB_KEY);


                movies.add(new Movie(title, thumbnail,  rating,synopsis, releaseDate, trailers, reviews,id));


            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movies JSON results", e);
        }

        return movies;
    }


}
