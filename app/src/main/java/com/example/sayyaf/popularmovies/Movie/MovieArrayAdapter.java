package com.example.sayyaf.popularmovies.Movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.sayyaf.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sayyaf on 11/26/2016.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private Context context;

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.imageview_gridview_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        String imageUrl = getItem(position).getImagethumbnail();
        Picasso.with(context)
                .load(imageUrl)
                .into(viewHolder.imageView);

        return convertView;

    }

    private class ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imageViewThumbnail);

        }
    }
}
