package com.example.sayyaf.popularmovies.Movie;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.sayyaf.popularmovies.Data.MovieContract;
import com.example.sayyaf.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Sayyaf on 12/7/2016.
 */
/*
Please note that alot  of the code here in this project is written based on the code and practices that I learned in the Android Basic: Networking course and Data Stroage
I applied the same design pattern and practices in this project.
*/
public class MovieCursorAdapter extends CursorAdapter {

    public MovieCursorAdapter(Context context, Cursor c) {

        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.imageview_gridview_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
       ImageView imageView = (ImageView) view.findViewById(R.id.imageViewThumbnail);

        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER));
        Picasso.with(context)
                .load(imageUrl)
                .into(imageView);
    }





}
