package com.example.sayyaf.popularmovies.Trailer;

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

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private Context context;

        public TrailerAdapter(Context context, List<Trailer> trailers) {
            super(context, 0, trailers);
            this.context = context;

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.trailers_listview_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.trailerTitle.setText("Trailer "+(position+1));


            return convertView;

        }

        private class ViewHolder {

            private TextView trailerTitle;

            public ViewHolder(View view) {

                trailerTitle=(TextView)view.findViewById(R.id.trailerTitleTextView);

            }
    }
}
