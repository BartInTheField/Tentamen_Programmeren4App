package com.sleintrab.movierental.Volley;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by barti on 05/24/17.
 */

public class JSONObjectRequest extends JsonObjectRequest {

    private final String SHAREDACCESTOKEN = "ACCESSTOKEN";
    private SharedPreferences tokenPref;
    private Context context;

    public JSONObjectRequest(int method,
                             String url,
                             JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener,
                             Context context){
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        tokenPref = context.getSharedPreferences(SHAREDACCESTOKEN, Context.MODE_PRIVATE);
        headers.put("Content-type", "application/json; charset=utf-8");
        headers.put("X-Access-Token" , tokenPref.getString("token", ""));
        return headers;
    }
}
