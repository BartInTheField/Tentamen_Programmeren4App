package com.sleintrab.movierental.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sleintrab.movierental.BuildConfig;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.Volley.JSONObjectRequest;
import com.sleintrab.movierental.Volley.VolleyRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Niels on 6/15/2017.
 */

public class MovieAPI implements Response.Listener, Response.ErrorListener {

    private final String URL = BuildConfig.SERVER_URL + "/films";

    private SharedPreferences accesToken;
    private SharedPreferences.Editor accesTokenEdit;

    private RequestQueue mQueue;

    private OnMoviesAvailable listener;
    private Context context;

    public MovieAPI(Context context,OnMoviesAvailable listener){
        this.context = context;
        this.listener = listener;

        mQueue = VolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public void retrieveMovies(){
        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.GET,
                URL,
                new JSONObject(),
                this,
                this);
        req.setTag("MoviesTAG");
        mQueue.add(req);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.networkResponse.statusCode == 400) {
            Toasty.error(context, "Cannot find any movies.", Toast.LENGTH_SHORT).show();
        } else {
            error.printStackTrace();
            Toasty.error(context, "Failed to retrieve movies", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Object response) {
        JSONObject jsonResponse;
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            jsonResponse = new JSONObject(response.toString());
            JSONArray moviesArray = jsonResponse.getJSONArray("Movies");
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                Movie movie = new Movie(
                    movieObject.optInt("film_id"),
                    movieObject.optString("title"),
                    movieObject.optString("description"),
                    movieObject.optInt("release_year"),
                    movieObject.optInt("rental_duration"),
                    movieObject.optDouble("rental_rate"),
                    movieObject.optInt("length"),
                    movieObject.optDouble("replacement_cost"),
                    movieObject.optString("rating"),
                    movieObject.optString("special_features")
                );
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onMoviesAvailable(movies);
    }

    public interface OnMoviesAvailable{
        void onMoviesAvailable(ArrayList<Movie> movies);
    }
}
