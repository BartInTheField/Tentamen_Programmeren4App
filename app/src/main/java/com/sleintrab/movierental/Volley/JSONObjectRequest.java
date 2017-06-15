package com.sleintrab.movierental.Volley;

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

    public JSONObjectRequest(int method,
                             String url,
                             JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener){
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=utf-8");
        return headers;
    }
}
