package com.example.sayyaf.popularmovies.Trailer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sayyaf on 12/6/2016.
 */

public class Trailer implements Parcelable {
    public Trailer(String url) {

        this.url = url;
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    protected Trailer(Parcel in) {
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
