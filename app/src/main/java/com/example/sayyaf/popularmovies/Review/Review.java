package com.example.sayyaf.popularmovies.Review;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sayyaf on 12/6/2016.
 */
public class Review implements Parcelable {
    private String review;
   private String author;

    public Review(String review, String author) {
        this.review = review;
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.review);
        dest.writeString(this.author);
    }

    protected Review(Parcel in) {
        this.review = in.readString();
        this.author = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
