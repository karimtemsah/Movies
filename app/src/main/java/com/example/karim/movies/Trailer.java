package com.example.karim.movies;

/**
 * Created by karim on 05.09.16.
 */
public class Trailer {
    String name;
    String id;

    public Trailer(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
