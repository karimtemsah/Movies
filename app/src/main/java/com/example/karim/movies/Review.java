package com.example.karim.movies;

/**
 * Created by karim on 05.09.16.
 */
public class Review {
    String author;
    String value;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Review(String author, String value) {

        this.author = author;
        this.value = value;
    }
}
