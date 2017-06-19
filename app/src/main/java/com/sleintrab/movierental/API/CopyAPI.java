package com.sleintrab.movierental.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sleintrab.movierental.BuildConfig;
import com.sleintrab.movierental.DomainModel.Copy;
import com.sleintrab.movierental.R;
import com.sleintrab.movierental.Volley.JSONObjectRequest;
import com.sleintrab.movierental.Volley.VolleyRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Niels on 6/17/2017.
 */

public class CopyAPI implements Response.Listener, Response.ErrorListener {

    private String URL;
    private final String TAG = getClass().getSimpleName();
    private RequestQueue mQueue;

    private OnCopiesAvailable listener;
    private NoCopiesAvailable errorListener;
    private Context context;

    public CopyAPI(Context context,OnCopiesAvailable listener, NoCopiesAvailable errorListener){
        this.context = context;
        this.listener = listener;
        this.errorListener = errorListener;

        mQueue = VolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public void retrieveCopies(int filmID){
        URL = BuildConfig.SERVER_URL + "copies/" + filmID;
        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.GET,
                URL,
                new JSONObject(),
                this,
                this,
                context);
        req.setTag("MoviesTAG");
        mQueue.add(req);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.networkResponse.statusCode == 400) {
            errorListener.noCopiesAvailable();
        } else {
            Log.e(TAG,error.getMessage());
            Toasty.error(context, context.getResources().getString(R.string.failedCopies), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Object response) {
        JSONObject jsonResponse;
        ArrayList<Copy> copies = new ArrayList<>();
        try {
            jsonResponse = new JSONObject(response.toString());
            JSONArray copiesArray = jsonResponse.getJSONArray("Copies");
            for (int i = 0; i < copiesArray.length(); i++) {
                JSONObject copyObject = copiesArray.getJSONObject(i);
                copies.add(new Copy(copyObject.optInt("inventory_id"), copyObject.optInt("film_id")));
            }
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }
        listener.onCopiesAvailable(copies);
    }

    public interface OnCopiesAvailable{
        void onCopiesAvailable(ArrayList<Copy> copies);
    }

    public interface NoCopiesAvailable{
        void noCopiesAvailable();
    }
}
