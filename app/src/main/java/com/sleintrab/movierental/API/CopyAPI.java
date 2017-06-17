package com.sleintrab.movierental.API;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sleintrab.movierental.BuildConfig;
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

    private RequestQueue mQueue;

    private OnCopyAvailable listener;
    private NoCopyAvailable errorListener;
    private Context context;

    public CopyAPI(Context context,OnCopyAvailable listener, NoCopyAvailable errorListener){
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
            errorListener.noCopyAvailable();
        } else {
            error.printStackTrace();
            Toasty.error(context, "Failed to retrieve copies", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Object response) {
        JSONObject jsonResponse;
        ArrayList<Integer> copyIDs = new ArrayList<>();
        try {
            jsonResponse = new JSONObject(response.toString());
            JSONArray copiesArray = jsonResponse.getJSONArray("Copies");
            for (int i = 0; i < copiesArray.length(); i++) {
                JSONObject copyObject = copiesArray.getJSONObject(i);
                copyIDs.add(copyObject.optInt("inventory_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onCopyAvailable(copyIDs.get(0));
    }

    public interface OnCopyAvailable{
        void onCopyAvailable(int copyID);
    }

    public interface NoCopyAvailable{
        void noCopyAvailable();
    }
}
