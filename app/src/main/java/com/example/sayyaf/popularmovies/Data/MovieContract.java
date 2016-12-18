package com.example.sayyaf.popularmovies.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sayyaf on 12/6/2016.
 */
/*
Please note that alot  of the code here in this project is written based on the code and practices that I learned in the Android Basic: Networking course and Data Stroage
I applied the same design pattern and practices in this project.
*/
public final class MovieContract {

    private MovieContract() {
    }



    public static final class MovieEntry implements BaseColumns {
        public static final String CONTENT_AUTHORITY = "com.example.sayyaf.popularmovies";
        public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_MOVIES = "movies";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);
       public final static String id=BaseColumns._ID;
        public final static String _ID="id";
        public final static String TABLE_NAME = "movies";
        public final static String COLUMN_MOVIE_TITLE = "title";
        public final static String COLUMN_MOVIE_POSTER = "poster";
        public final static String COLUMN_MOVIE_SYNOPSIS = "synopsis";
        public final static String COLUMN_MOVIE_USER_RATING = "user_rating";
        public final static String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

    }
}