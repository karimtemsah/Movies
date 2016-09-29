package com.example.karim.movies;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karim on 27.07.16.
 */


public class Movie extends SugarRecord{
    String poster_path;
    String overview;
    String release_date;
    String title;
    double vote_count;
    double vote_average;
    int movie_id;

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public Movie(){

    }

    public Movie(String overview, String poster_path, String release_date, String title, double vote_count, double vote_average, int movie_id) {
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.movie_id = movie_id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public double getVote_count() {
        return vote_count;
    }

    public void setVote_count(double vote_count) {
        this.vote_count = vote_count;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public String toString() {
        JSONObject json  = new JSONObject();
        try {
            json.put("poster_path",poster_path);
            json.put("overview",overview);
            json.put("release_date",release_date);
            json.put("title",title);
            json.put("vote_count",vote_count);
            json.put("vote_average",vote_average);
            json.put("movie_id",movie_id);
            return  json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
