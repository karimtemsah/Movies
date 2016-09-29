package com.example.karim.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by karim on 02.09.16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    final static String LOG = "DB";
    static final String DATABASE_NAME = "movies.db";
    public static final String TABLE_MOVIES = "movie";
    public static final String POSTER_PATH="poster_path";
    public static final String OVERVIEW="overview";
    public static final String RELEASE_DATE="release_date";
    public static final String TITLE="title";
    public static final String POPULARITY="popularity";
    public static final String VOTE_COUNT="vote_count";
    public static final String VOTE_AVERAGE="vote_average";
    public static final String KEY="id";
    private static final String CREATE_TABLE_Movie = "CREATE TABLE "
            + TABLE_MOVIES +
            "(" + KEY + " INTEGER PRIMARY KEY,"
            + TITLE + " TEXT,"
            +  OVERVIEW + " TEXT,"
            + POSTER_PATH + " TEXT,"
            + RELEASE_DATE + " TEXT,"
            //+POPULARITY + " INTEGER,"
            +VOTE_COUNT + " REAL NOT NULL,"
            +VOTE_AVERAGE + " REAL NOT NULL)";



    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Movie);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);

    }

    public long createMovie(Movie m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, m.getTitle());
        values.put(OVERVIEW,m.getOverview());
        values.put(POSTER_PATH,m.getPoster_path());
        values.put(RELEASE_DATE,m.getRelease_date());
      //  values.put(POPULARITY,m.getPopularity());
        values.put(VOTE_COUNT, m.getVote_count());
        values.put(VOTE_AVERAGE, m.getVote_average());
        return db.insert(TABLE_MOVIES,null,values);
    }
    public   Movie getMovie(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES + " WHERE "
                + TITLE + " = " + title;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
            Movie m = new Movie();
            m.setTitle(title);
            m.setOverview(c.getString(c.getColumnIndex(OVERVIEW)));
            m.setPoster_path(c.getString(c.getColumnIndex(POSTER_PATH)));
          //  m.setPopularity(c.getString(c.getColumnIndex(POPULARITY)));
            m.setRelease_date(c.getString(c.getColumnIndex(RELEASE_DATE)));
            m.setVote_count(c.getDouble(c.getColumnIndex(VOTE_COUNT)));
            m.setVote_average(c.getDouble(c.getColumnIndex(VOTE_AVERAGE)));
            return m;

        } else {
            return null;
        }

    }
    public  ArrayList<Movie> fetchAllMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Movie m = new Movie();
                m.setTitle(c.getString(c.getColumnIndex(TITLE)));
                m.setOverview(c.getString(c.getColumnIndex(OVERVIEW)));
                m.setPoster_path(c.getString(c.getColumnIndex(POSTER_PATH)));
                //  m.setPopularity(c.getString(c.getColumnIndex(POPULARITY)));
                m.setRelease_date(c.getString(c.getColumnIndex(RELEASE_DATE)));
                m.setVote_count(c.getDouble(c.getColumnIndex(VOTE_COUNT)));
                m.setVote_average(c.getDouble(c.getColumnIndex(VOTE_AVERAGE)));

                // adding to todo list
                movies.add(m);
            } while (c.moveToNext());
        }
        return  movies;
    }
    public   void deleteMovie(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, TITLE + " = ?",
                new String[] { title });
    }
    public  void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
