package com.example.sayyaf.popularmovies.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sayyaf.popularmovies.R;

import java.util.List;

/**
 * Created by Sayyaf on 12/6/2016.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {
    private Context context;

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
        this.context = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.reviews_listview_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Review review=getItem(position);

        viewHolder.author.setText(review.getAuthor());
        viewHolder.content.setText(review.getReview());


        return convertView;

    }

    private class ViewHolder {

        private TextView author;
        private TextView content;

        public ViewHolder(View view) {

            author=(TextView)view.findViewById(R.id.authorTextView);
            content=(TextView)view.findViewById(R.id.contentTextView);

        }
    }
}
