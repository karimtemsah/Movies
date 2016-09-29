package com.example.karim.movies;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by karim on 05.09.16.
 */
public class TrailerAdapter extends BaseAdapter {
    ArrayList<Trailer> trailerNames;
    private final LayoutInflater mInflater;
    Context context;
    private static final int viewCounts = 2;
    private static final int detailAdapter = 0;
    private static final int trailerAdapter = 1;
    private static final int VIEW_TYPE_COUNT = 2;
    Movie movie;
    ArrayList<Review> reviewNames;

    public static class ViewHolderTrailer {
        public final ImageView trailerImg;
        public final TextView trailerName;
        public ViewHolderTrailer(View view) {
            trailerImg = (ImageView) view.findViewById(R.id.trailer_icon);
            trailerName = (TextView) view.findViewById(R.id.trailer_text);
        }
    }
    public static class ViewHolder {
        public final ImageView poster;
        public final TextView title;
        public final TextView date;
        public final TextView vote;
        public final TextView rate;
        public final TextView overview;
        public final TextView trailer;
        public final Button fav;
        public final TextView review;
        public final TextView textView2;
        //public final ImageView trailerImg;
        //public final TextView trailerName;

        public ViewHolder(View view) {
            poster = (ImageView) view.findViewById(R.id.poster);
            title = (TextView) view.findViewById(R.id.title_val);
            date = (TextView) view.findViewById(R.id.date_val);
            vote = (TextView) view.findViewById(R.id.vote_val);
            rate = (TextView) view.findViewById(R.id.rate_val);
            overview = (TextView) view.findViewById(R.id.overview_val);
            trailer = (TextView) view.findViewById(R.id.trailer_key);
            fav = (Button) view.findViewById(R.id.fav);
            review = (TextView) view.findViewById(R.id.reviews);
            textView2 = (TextView) view.findViewById(R.id.textView2);
            //trailerImg = (ImageView) view.findViewById(R.id.trailer_icon);
            //trailerName = (TextView) view.findViewById(R.id.trailer_text);
        }
    }


    public TrailerAdapter(Context context,ArrayList<Trailer> names,Movie m, ArrayList<Review> rev) {
       this.context = context;
        trailerNames = names;
        mInflater = LayoutInflater.from(context);
        movie = m;
        reviewNames = rev;
    }
    public void setReviews(ArrayList<Review> r) {
        this.reviewNames = r;

    }
    public void notOnline() {

    }

    @Override
    public int getViewTypeCount() {
        return viewCounts;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
          return detailAdapter;
        } else {
            return trailerAdapter;
        }
    }
    public void setMovie(Movie m) {
        movie = m;
    }

    @Override
    public int getCount() {
        return trailerNames.size() + 1;
    }

    @Override
    public Trailer getItem(int i) {
        return trailerNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void setTrailerNames(ArrayList<Trailer> n) {
        trailerNames = n;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //return null;
        View v = view;
        int type = getItemViewType(i);
         ViewHolder viewHolder = null;
        ViewHolderTrailer viewHolderTrailer = null;
        if (v == null) {
            switch (type) {
                case (detailAdapter):
                    v = mInflater.inflate(R.layout.detail_item,null);
                    viewHolder = new ViewHolder(v);
                    break;
                case (trailerAdapter):
                    v = mInflater.inflate(R.layout.trailer_item,null);
                    viewHolderTrailer = new ViewHolderTrailer(v);
                    break;

            }
        }
         else {
            viewHolder = new ViewHolder(v);
            viewHolderTrailer = new ViewHolderTrailer(v);
        }
        if (type == detailAdapter && movie != null) {
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.title.setText(movie.getTitle());
            viewHolder.textView2.setVisibility(View.VISIBLE);
            viewHolder.overview.setText(movie.getOverview());
            viewHolder.rate.setText(""+movie.getVote_average()+"/10");
            viewHolder.vote.setText(""+ movie.getVote_count());
            viewHolder.date.setText(movie.getRelease_date());
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path()).into(viewHolder.poster);
            viewHolder.trailer.setText("Trailers:");
            viewHolder.fav.setVisibility(View.VISIBLE);
            Movie movieToDelete;
            List<Movie> allMovies =  Movie.find(Movie.class, "title = ?",movie.getTitle());
            movieToDelete = null;
            if (allMovies.size() > 0) {
                movieToDelete = allMovies.get(0);
            }

            if (movieToDelete == null) {
                viewHolder.fav.setText("Mark as favourite");
            } else {
                viewHolder.fav.setText(R.string.favourite);
            }
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalViewHolder.fav.getText().toString().equalsIgnoreCase("Mark as favourite")) {
                        movie.save();
                        finalViewHolder.fav.setText(R.string.favourite);
                        //favourite.setBackgroundColor(R.color.yellow);
                    } else {
                        Movie movieToDelete;
                        List<Movie> allMovies =  Movie.find(Movie.class, "title = ?",movie.getTitle());
                        movieToDelete = null;
                        if (allMovies.size() > 0) {
                            movieToDelete = allMovies.get(0);
                        }
                        if (MainActivity.mTwoPane && TrailerAdapter.this.getPreference().equalsIgnoreCase("fav")) {
                            MainActivityFragment.adapter.removeMovie(movieToDelete);
                            MainActivityFragment.adapter.notifyDataSetChanged();
                            DetailActivityFragment.trailerAdapter = new TrailerAdapter(context,new ArrayList<Trailer>(),null,reviewNames);
                            DetailActivityFragment.trailerList.setAdapter(DetailActivityFragment.trailerAdapter);
                            DetailActivityFragment.trailerAdapter.notifyDataSetChanged();
                            if (MainActivityFragment.adapter.getCount() == 0) {
                                MainActivityFragment.adapter = new ImageAdapter(MainActivityFragment.rootView.getContext(), new ArrayList<Movie>());
                                MainActivityFragment.gridview.setAdapter(MainActivityFragment.adapter);
                                MainActivityFragment.t.setText("There are no favourites chosen");
                                MainActivityFragment.gridview.setEmptyView(MainActivityFragment.t);
                            }
                        }
                        movieToDelete.delete();
                        finalViewHolder.fav.setText("Mark as favourite");
                        // favourite.setBackgroundColor(R.color.grey);
                    }
                }
            });
            viewHolder.review.setText("see reviews");
            viewHolder.review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View view = li.inflate(R.layout.review_list, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(view);

                    ListView lv2 = (ListView) view.findViewById(R.id.rev);
                    ReviewAdapter adapter = new ReviewAdapter(context, reviewNames);
                    lv2.setAdapter(adapter);
                    alertDialogBuilder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();


                  /* Toast.makeText(
                            context,
                            reviewNames.get(0).getAuthor(),
                            Toast.LENGTH_SHORT
                    ).show();*/

                }
            });

        } else if (type == trailerAdapter && movie != null) {
            viewHolderTrailer.trailerImg.setImageResource(R.mipmap.youtube);
            viewHolderTrailer.trailerName.setText(getItem(i - 1).getName());
            final String id = getItem(i - 1).getId();
            Log.d("ID", id);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {*/
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + id));
                    Log.d("URL", "http://www.youtube.com/watch?v=" + id);
                        context.startActivity(intent);
                    //}
                }
            });
        }
        return v;

    }
    private String getPreference() {
        SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(context);
        String sort = sharedPrefs.getString(
                context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_popular));
        return sort;
    }

}
