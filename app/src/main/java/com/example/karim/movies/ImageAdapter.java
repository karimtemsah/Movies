package com.example.karim.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim on 27.07.16.
 */
public class ImageAdapter extends BaseAdapter {

    private ArrayList<Movie> mItems;
    private final LayoutInflater mInflater;
    Context context;

    public ImageAdapter(Context context,ArrayList<Movie> items) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mItems = items;
       // mItems.add(new Item("Red",       R.drawable.sample_0));
       // mItems.add(new Item("Magenta",   R.drawable.sample_1));
        //mItems.add(new Item("Dark Gray", R.drawable.sample_2));
        //mItems.add(new Item("Gray",      R.drawable.sample_3));
        //mItems.add(new Item("Green",     R.drawable.sample_4));
        //mItems.add(new Item("Cyan",      R.drawable.sample_4));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Movie getItem(int i) {

        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public String getItemTitle(int i) {

        return  mItems.get(i).getTitle();
    }
    public void removeMovie(Movie movie) {
        for (Movie m: mItems) {
            if (m.getTitle().equalsIgnoreCase(movie.getTitle())) {
                mItems.remove(m);
                break;
            }
        }
    }


    public ArrayList<Movie> getmItems() {
        return mItems;
    }
    public void setItems(ArrayList<Movie> m) {
        this.mItems = m;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Movie item = getItem(i);

        //picture.setImageResource(item.drawableId);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + item.getPoster_path()).into( picture);

        name.setText(item.getTitle());

        return v;
    }

    private static class Item {
        public final String name;
        public final int drawableId;

        Item(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }

}
