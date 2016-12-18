package com.example.sayyaf.popularmovies.Movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sayyaf.popularmovies.Review.Review;
import com.example.sayyaf.popularmovies.Trailer.Trailer;

import java.util.List;

/**
 * Created by Sayyaf on 11/26/2016.
 */

public class Movie implements Parcelable {


    private String title;
    private String Imagethumbnail;
    private String synopsis;
    private double rating;
    private String releaseDate;
    private List<Trailer>trailers;
    private List<Review>reviews;
    private int id;

    public Movie(String title, String imagethumbnail, double rating, String synopsis, String releaseDate, List<Trailer> trailers, List<Review> reviews, int id) {
        this.title = title;
        Imagethumbnail = imagethumbnail;
        this.rating = rating;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.trailers = trailers;
        this.reviews = reviews;
        this.id = id;
    }

    public Movie(int id, double rating, String synopsis, String imagethumbnail, String title, String releaseDate) {
        this.id = id;
        this.rating = rating;
        this.synopsis = synopsis;
        Imagethumbnail = imagethumbnail;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getImagethumbnail() {
        return Imagethumbnail;
    }

    public void setImagethumbnail(String imagethumbnail) {
        Imagethumbnail = imagethumbnail;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.Imagethumbnail);
        dest.writeString(this.synopsis);
        dest.writeDouble(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeTypedList(this.trailers);
        dest.writeTypedList(this.reviews);
        dest.writeInt(this.id);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.Imagethumbnail = in.readString();
        this.synopsis = in.readString();
        this.rating = in.readDouble();
        this.releaseDate = in.readString();
        this.trailers = in.createTypedArrayList(Trailer.CREATOR);
        this.reviews = in.createTypedArrayList(Review.CREATOR);
        this.id = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
