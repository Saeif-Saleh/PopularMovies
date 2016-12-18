package com.example.sayyaf.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sayyaf.popularmovies.Data.MovieContract.MovieEntry;

import static android.R.attr.id;
import static com.example.sayyaf.popularmovies.Data.MovieContract.MovieEntry.CONTENT_AUTHORITY;
import static com.example.sayyaf.popularmovies.Data.MovieContract.MovieEntry.PATH_MOVIES;

/**
 * Created by Sayyaf on 12/7/2016.
 */
/*
Please note that alot  of the code here in this project is written based on the code and practices that I learned in the Android Basic: Networking course and Data Stroage
I applied the same design pattern and practices in this project.
*/
public class MovieProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;
    private MovieDBHelper movieDBHelper;
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, MovieEntry.PATH_MOVIES, 1);
    }

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = movieDBHelper.getReadableDatabase();
        Cursor cursor;
      //  projection= new String[]{MovieEntry._ID+" AS _id",MovieEntry.COLUMN_MOVIE_SYNOPSIS,MovieEntry.COLUMN_MOVIE_RELEASE_DATE,MovieEntry.COLUMN_MOVIE_POSTER,MovieEntry.COLUMN_MOVIE_USER_RATING};
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:


                cursor = database.query(MovieEntry.TABLE_NAME, null, null, null, null, null, null, null);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot Query Unkown URI " + uri);
        }
         // update the cursor when the watched data specified by the uri  is changed
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return insertMovie(uri, contentValues);

            default:
                throw new IllegalArgumentException("Cannot Query Unkown URI " + uri);

        }


    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private Uri insertMovie(Uri uri, ContentValues contentValues) {

        SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        try {
            long id = db.insertOrThrow(MovieEntry.TABLE_NAME, null, contentValues);
        } catch (SQLiteConstraintException e) {

            return ContentUris.withAppendedId(uri, -3);
        }

        if (id == -1) {
            Log.e("MSG", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }
}
