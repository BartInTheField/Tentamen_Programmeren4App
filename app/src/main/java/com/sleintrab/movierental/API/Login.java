package com.sleintrab.movierental.API;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sleintrab.movierental.HomeActivity;
import com.sleintrab.movierental.Volley.JSONObjectRequest;
import com.sleintrab.movierental.Volley.VolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by barti on 13-Jun-17.
 */


public class Login implements Response.ErrorListener, Response.Listener {

    private final String SHAREDACCESTOKEN = "ACCESTOKEN";
    private final String URL = "URLFORAPI";

    private SharedPreferences accesToken;
    private SharedPreferences.Editor accesTokenEdit;

    private JSONObject jsonResponse;
    private RequestQueue mQueue;

    private static Context context;

    public Login(Context context) {
        this.context = context;

        accesToken = context.getSharedPreferences(SHAREDACCESTOKEN, Context.MODE_PRIVATE);
        accesTokenEdit = accesToken.edit();


        mQueue = VolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public void LoginAccount(String email, String password) {

        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.POST,
                URL,
                makeBodyJSON(email, password),
                this,
                this);
        req.setTag("LoginTAG");
        mQueue.add(req);
    }

    private JSONObject makeBodyJSON(String email, String password){
        JSONObject jsonBody = new JSONObject();

        try{
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonBody;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.networkResponse.statusCode == 400) {
            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        } else {
            error.printStackTrace();
            Toast.makeText(context, "Failed to log in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Object response) {
        try {
            jsonResponse = new JSONObject(response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        accesTokenEdit.putString("token", jsonResponse.optString("token"));
        accesTokenEdit.putString("email", jsonResponse.optString("email"));
        accesTokenEdit.commit();
        Intent i = new Intent(context, HomeActivity.class);
        context.startActivity(i);
    }
}
