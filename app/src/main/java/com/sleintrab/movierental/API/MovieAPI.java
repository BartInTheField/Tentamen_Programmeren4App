package com.sleintrab.movierental.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by Niels on 6/15/2017.
 */

public class MovieAPI implements Response.Listener, Response.ErrorListener {

    private String URL;
    private final String SHAREDACCESTOKEN = "ACCESSTOKEN";

    private SharedPreferences accesToken;

    private RequestQueue mQueue;

    private OnMoviesAvailable listener;
    private NoMoviesAvailable listenerTwo;
    private Context context;
    private Boolean hasUserID = false;

    public MovieAPI(Context context,OnMoviesAvailable listener){
        this.context = context;
        this.listener = listener;

        URL = BuildConfig.SERVER_URL + "films";

        mQueue = VolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public MovieAPI(Context context,OnMoviesAvailable listener, NoMoviesAvailable listenerTwo,  int userID){
        this.context = context;
        this.listener = listener;
        this.listenerTwo = listenerTwo;

        accesToken = context.getSharedPreferences(SHAREDACCESTOKEN, Context.MODE_PRIVATE);
        URL = BuildConfig.SERVER_URL + "rentals/" + userID;
        hasUserID = true;

        mQueue = VolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public void retrieveMovies() throws AuthFailureError {
        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.GET,
                URL,
                new JSONObject(),
                this,
                this,
                context);
        req.setTag("MoviesTAG");
        if (hasUserID){
            req.getHeaders().put("X-Access-Token", accesToken.getString("token", ""));
        }
        Log.i("HEADERS", req.getHeaders().toString());
        mQueue.add(req);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.networkResponse.statusCode == 400) {
            if (!hasUserID){
                Toasty.error(context, "Cannot find any movies.", Toast.LENGTH_SHORT).show();
            } else {
                listenerTwo.noMoviesAvailable();
            }
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
                if (hasUserID){
                    movie.setReturnDate(movieObject.optString("return_date"));
                    movie.setInventoryID(movieObject.optInt("inventory_id"));
                }
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

    public interface NoMoviesAvailable{
        void noMoviesAvailable();
    }
}
