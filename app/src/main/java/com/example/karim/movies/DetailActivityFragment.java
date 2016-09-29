package com.example.karim.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim on 10.09.16.
 */
public class DetailActivityFragment extends Fragment {
    Movie movieToSave=null;
    static ListView trailerList;
    static TrailerAdapter trailerAdapter;
    ArrayList<Trailer> trailerNames = new ArrayList<Trailer>();
    ArrayList<Review> reviews = new ArrayList<Review>();
    private String movieToStr;
    private String pref;
    View rootView;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView =  inflater.inflate(R.layout.fragment_detail, container, false);
        pref = this.getPreference();
        Bundle arguments = getArguments();
        if (arguments != null) {
            movieToStr = arguments.getString(Intent.EXTRA_TEXT);

            try {
                JSONObject json = new JSONObject(movieToStr);
                movieToSave = new Movie(json.getString("overview"), json.getString("poster_path")
                        , json.getString("release_date"), json.getString("title"),
                        json.getDouble("vote_count"), json.getDouble("vote_average"), json.getInt("movie_id"));

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        trailerList = (ListView)rootView.findViewById(R.id.trailer_list);
        trailerAdapter = new TrailerAdapter(rootView.getContext(),trailerNames,movieToSave,reviews);
        trailerList.setAdapter(trailerAdapter);


        return rootView;
    }

    private String getPreference() {
        SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = sharedPrefs.getString(
                getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        return sort;
    }

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!pref.equalsIgnoreCase(getPreference())){
            outState = null;
        }
        super.onSaveInstanceState(outState);
    }*/
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (!pref.equalsIgnoreCase(getPreference())) {
           // pref  = getPreference();
            movieToSave = null;
            trailerAdapter = new TrailerAdapter(rootView.getContext(),trailerNames,movieToSave,reviews);
            trailerList.setAdapter(trailerAdapter);
           trailerAdapter.notifyDataSetChanged();
        }

         if (isOnline() && movieToSave != null) {
            fetchTrailers();
            fetchReviews();
        }
    }

    public void fetchTrailers() {
        final String TAG = "Volley2";
        final String  tag_string_req = "trailer_req";

        final String url = "http://api.themoviedb.org/3/movie/";



        StringRequest strReq = new StringRequest(Request.Method.GET,
                url + movieToSave.getMovie_id() + "/videos?api_key=" + BuildConfig.API_KEY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                ArrayList<Trailer> result = DetailActivityFragment.this.fetchTrailersFromJson(response);
                if (result != null) {
                    // progressBar.setVisibility(View.INVISIBLE);
                    trailerAdapter.setTrailerNames(result);
                    trailerAdapter.notifyDataSetChanged();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public ArrayList<Trailer> fetchTrailersFromJson(String response) {
        ArrayList<Trailer> res = new ArrayList<Trailer>();
        JSONObject main = null;

        try {
            main = new JSONObject(response);
            JSONArray array = main.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                String type = json.getString("type");
                if (type.equalsIgnoreCase("Trailer")) {
                    res.add(new Trailer(json.getString("name"), json.getString("key")));
                }
            }
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void fetchReviews() {
        final String TAG = "Volley3";
        final String  tag_string_req = "review_req";

        final String url = "http://api.themoviedb.org/3/movie/";



        StringRequest strReq = new StringRequest(Request.Method.GET,
                url + movieToSave.getMovie_id() + "/reviews?api_key=" + BuildConfig.API_KEY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                ArrayList<Review> result = DetailActivityFragment.this.fetchReviewsFromJson(response);
                if (result != null) {
                    // progressBar.setVisibility(View.INVISIBLE);
                    trailerAdapter.setReviews(result);
                    Log.d("Review", "5alas JSON");

                    trailerAdapter.notifyDataSetChanged();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public ArrayList<Review> fetchReviewsFromJson(String response) {
        ArrayList<Review> res = new ArrayList<Review>();
        JSONObject main = null;

        try {
            main = new JSONObject(response);
            JSONArray array = main.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);


                res.add(new Review(json.getString("author"), json.getString("content")));


            }
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
