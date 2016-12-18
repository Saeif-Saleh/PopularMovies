package com.example.sayyaf.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sayyaf.popularmovies.Data.MovieContract.MovieEntry;


/**
 * Created by Sayyaf on 12/6/2016.
 */
/*
Please note that alot  of the code here in this project is written based on the code and practices that I learned in the Android Basic: Networking course and Data Stroage
I applied the same design pattern and practices in this project.
*/
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="popularmovies.db";
    private static final int DATBASE_VERSION=1;
    String SQL_CREATE_MOVIE_TABLE= "CREATE TABLE "+ MovieEntry.TABLE_NAME+" ("+MovieEntry.id+" INTEGER PRIMARY KEY ,"+MovieEntry._ID +" INTEGER UNIQUE  ,"
            +MovieEntry.COLUMN_MOVIE_TITLE+" TEXT NOT NULL,"+MovieEntry.COLUMN_MOVIE_SYNOPSIS+
            " TEXT NOT NULL,"+ MovieEntry.COLUMN_MOVIE_RELEASE_DATE+" TEXT NOT NULL,"+
            MovieEntry.COLUMN_MOVIE_POSTER+" TEXT NOT NULL,"+MovieEntry.COLUMN_MOVIE_USER_RATING+
            " TEXT NOT NULL);";
    public MovieDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+MovieEntry.TABLE_NAME+";");
        onCreate(db);

    }
}
