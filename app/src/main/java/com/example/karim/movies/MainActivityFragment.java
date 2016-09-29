package com.example.karim.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    static ImageAdapter adapter;
    ArrayList<Movie> movies = new ArrayList<Movie>();
    final String api_key = "622e75c44c3e7a679206c1891fac870b";
    //ProgressBar progressBar;
    static TextView t;
    //Button b;
    static GridView gridview;
    static View rootView;
    SwipeRefreshLayout mySwipeRefreshLayout;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Movie m);
    }
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        t = (TextView) rootView.findViewById(R.id.textView);
        t.setVisibility(View.INVISIBLE);

        gridview = (GridView) rootView.findViewById(R.id.gridview);
        adapter = new ImageAdapter(rootView.getContext(), movies);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(getActivity(), adapter.getItemTitle(position),
                //      Toast.LENGTH_LONG).show();
                //String res = adapter.getItem(position).toString();
              //  Log.d("JSON", res);
                Movie mm = adapter.getItem(position);
               // Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, res);
                //startActivity(intent);
                ((Callback) getActivity())
                        .onItemSelected(mm);
                mPosition = position;
            }
        });

        mySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        // myUpdateOperation();
                        MainActivityFragment.this.onStartHelper();

                    }
                }
        );
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void onStartHelper(){
        if (isOnline() && !this.getPreference().equalsIgnoreCase("fav")) {
            mySwipeRefreshLayout.setRefreshing(true);
            Log.d("online", "onStart: ");
            this.updateMovies();
        }
        mySwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onStart() {
        super.onStart();
        gridview.post(new Runnable() {
            @Override
            public void run() {
                gridview.smoothScrollToPosition(mPosition);
            }
        });

        if (this.getPreference().equalsIgnoreCase("fav")) {
           List<Movie> mm =  Movie.findWithQuery(Movie.class, "Select * from Movie");
            if (mm.size() == 0) {
                t.setText("There are no favourites chosen");
                adapter = new ImageAdapter(rootView.getContext(), movies);
                gridview.setAdapter(adapter);
                gridview.setEmptyView(t);
            }
            else {
                adapter.setItems((ArrayList<Movie>) mm);
                adapter.notifyDataSetChanged();
            }

        } else if (isOnline()) {
            mySwipeRefreshLayout.setRefreshing(true);
            Log.d("online", "onStart: ");
            this.updateMovies();
            mySwipeRefreshLayout.setRefreshing(false);

        } else {
            t.setText(R.string.no_intr);
            Log.d("offline", "onStart: ");
            adapter = new ImageAdapter(rootView.getContext(), movies);
            gridview.setAdapter(adapter);
            gridview.setEmptyView(t);



        }
    }



    private String getPreference() {
        SharedPreferences sharedPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = sharedPrefs.getString(
                getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        return sort;
    }

    private void updateMovies() {
       // FetchMovies fetch = new FetchMovies();

        this.fetchMovies(this.getPreference());
    }

    public void fetchMovies(String s) {
        final String TAG = "Volley";
        final String  tag_string_req = "string_req";

        final String url = "http://api.themoviedb.org/3/movie/";



        StringRequest strReq = new StringRequest(Request.Method.GET,
                url + s + "?api_key=" + BuildConfig.API_KEY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
               ArrayList<Movie> movies =  MainActivityFragment.this.getMoviesDataFromJson(response);
                if (movies != null) {
                    // progressBar.setVisibility(View.INVISIBLE);
                    adapter.setItems(movies);
                    adapter.notifyDataSetChanged();
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

    private ArrayList<Movie> getMoviesDataFromJson(String forecastJsonStr) {
        ArrayList<Movie> temp = new ArrayList<Movie>();
        JSONObject main = null;
        try {
            // Log.d("JSON", forecastJsonStr);
            main = new JSONObject(forecastJsonStr);

            JSONArray result = main.getJSONArray("results");
            for(int i = 0; i < result.length(); i++) {
                JSONObject next = result.getJSONObject(i);
                Movie m = new Movie(next.getString("overview"), next.getString("poster_path"), next.getString("release_date"),
                        next.getString("title"), next.getDouble("vote_count"),
                        next.getDouble("vote_average"),next.getInt("id"));
                temp.add(m);

            }

            return  temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
