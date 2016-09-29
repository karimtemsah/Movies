package com.example.karim.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by karim on 05.09.16.
 */
public class ReviewAdapter extends BaseAdapter {
    ArrayList<Review> reviews;
    Context context;
    private final LayoutInflater mInflater;
    public ReviewAdapter(Context context,ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Review getItem(int i) {
        return reviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        TextView author;
        TextView value;
        if (v == null) {
            v = mInflater.inflate(R.layout.review_item, viewGroup, false);
            v.setTag(R.id.rev_author, v.findViewById(R.id.rev_author));
            v.setTag(R.id.rev_val, v.findViewById(R.id.rev_val));
        }

        author = (TextView) v.getTag(R.id.rev_author);
        value = (TextView) v.getTag(R.id.rev_val);

        Review item = getItem(i);

        //picture.setImageResource(item.drawableId);


        author.setText(item.getAuthor());
        value.setText(item.getValue());

        return v;
    }
}
