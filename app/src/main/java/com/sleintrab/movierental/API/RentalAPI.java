package com.sleintrab.movierental.API;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sleintrab.movierental.BuildConfig;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.DomainModel.Rental;
import com.sleintrab.movierental.Volley.JSONObjectRequest;
import com.sleintrab.movierental.Volley.VolleyRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by barti on 17-Jun-17.
 */

public class RentalAPI implements Response.ErrorListener, Response.Listener {

    private final String URL = BuildConfig.SERVER_URL + "rentals/";

    private RequestQueue mQueue;

    private static Context context;

    private OnRentalSuccess listener = null;
    private OnRentalFailed errorListener = null;
    private OnRentalsAvailable getListener = null;
    private OnActiveRentalsAvailable activeRentalsListener = null;

    private Boolean isGETRequest = false;
    private Boolean isActiveRentalRequest = false;

    public RentalAPI(Context context, OnRentalSuccess listener, OnRentalsAvailable getListener, OnRentalFailed errorListener, OnActiveRentalsAvailable activeRentalsListener){
        this.context = context;
        this.listener = listener;
        this.getListener = getListener;
        this.errorListener = errorListener;
        this.activeRentalsListener = activeRentalsListener;
        mQueue = VolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public void handInRental(int customerID, int inventoryID) throws AuthFailureError{

        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.PUT,
                URL + customerID + "/" + inventoryID,
                new JSONObject(),
                this,
                this,
                context);
        req.setTag("HandInTAG");
        mQueue.add(req);
    }

    public void makeRental(int customerID, int inventoryID) throws AuthFailureError{
        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.POST,
                URL + customerID + "/" + inventoryID,
                new JSONObject(),
                this,
                this,
                context);
        req.setTag("makeRentalTAG");
        mQueue.add(req);
    }

    public void getRentals(int customerID) throws AuthFailureError {
        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.GET,
                URL + customerID,
                new JSONObject(),
                this,
                this,
                context);
        req.setTag("makeRentalTAG");
        isGETRequest = true;
        mQueue.add(req);
    }

    public void getActiveRentals() throws AuthFailureError {
        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.GET,
                URL + "activeRentals",
                new JSONObject(),
                this,
                this,
                context);
        req.setTag("activeRentalsTAG");
        mQueue.add(req);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        errorListener.onRentalFailed();

    }

    @Override
    public void onResponse(Object response) {
        if (isGETRequest){
            isGETRequest = false;

            JSONObject jsonResponse;
            ArrayList<Rental> rentals = new ArrayList<>();
            try {
                jsonResponse = new JSONObject(response.toString());
                JSONArray moviesArray = jsonResponse.getJSONArray("Movies");
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject rentalObject = moviesArray.getJSONObject(i);
                    Movie movie = new Movie(
                            rentalObject.optInt("film_id"),
                            rentalObject.optString("title"),
                            rentalObject.optString("description"),
                            rentalObject.optInt("release_year"),
                            rentalObject.optInt("rental_duration"),
                            rentalObject.optDouble("rental_rate"),
                            rentalObject.optInt("length"),
                            rentalObject.optDouble("replacement_cost"),
                            rentalObject.optString("rating"),
                            rentalObject.optString("special_features")
                    );
                    Rental rental = new Rental(movie,
                            rentalObject.optString("return_date"),
                            rentalObject.optInt("inventory_id"));
                    rentals.add(rental);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getListener.onRentalsAvailable(rentals);
        } else if (isActiveRentalRequest){
            JSONObject jsonResponse;
            ArrayList<Integer> inventoryIDs = new ArrayList<>();
            try {
                jsonResponse = new JSONObject(response.toString());
                JSONArray inventoryIDArray = jsonResponse.getJSONArray("Movies");
                for (int i = 0; i < inventoryIDArray.length(); i++) {
                    JSONObject idObject = inventoryIDArray.getJSONObject(i);
                    inventoryIDs.add(idObject.optInt("inventory_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            activeRentalsListener.onActiveRentalsAvailable(inventoryIDs);
        }else {
            listener.onRentalSuccess();
        }
    }

    public interface OnRentalSuccess {
        void onRentalSuccess();
    }

    public interface OnRentalsAvailable {
        void onRentalsAvailable(ArrayList<Rental> rentals);
    }

    public interface OnActiveRentalsAvailable{
        void onActiveRentalsAvailable(ArrayList<Integer> inventoryIDs);
    }

    public interface OnRentalFailed{
        void onRentalFailed();
    }
}
