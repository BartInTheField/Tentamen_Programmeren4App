package com.sleintrab.movierental.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sleintrab.movierental.BuildConfig;
import com.sleintrab.movierental.DomainModel.Customer;
import com.sleintrab.movierental.R;
import com.sleintrab.movierental.Volley.JSONObjectRequest;
import com.sleintrab.movierental.Volley.VolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

/**
 * Created by barti on 13-Jun-17.
 */


public class LoginAPI implements Response.ErrorListener, Response.Listener {

    private final String TAG = getClass().getSimpleName();

    private final String SHAREDACCESTOKEN = "ACCESSTOKEN";
    private final String URL = BuildConfig.SERVER_URL + "login";

    private SharedPreferences accesToken;
    private SharedPreferences.Editor accesTokenEdit;

    private JSONObject jsonResponse;
    private RequestQueue mQueue;

    private Context context;

    private OnLoginSuccess listener = null;

    public LoginAPI(Context context, OnLoginSuccess listener) {
        this.context = context;
        this.listener = listener;

        accesToken = context.getSharedPreferences(SHAREDACCESTOKEN, Context.MODE_PRIVATE);
        accesTokenEdit = accesToken.edit();


        mQueue = VolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public void loginAccount(String email, String password) {

        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.POST,
                URL,
                makeBodyJSON(email, password),
                this,
                this,
                context);
        req.setTag("LoginTAG");
        mQueue.add(req);
    }

    private JSONObject makeBodyJSON(String email, String password){
        JSONObject jsonBody = new JSONObject();

        try{
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }

        return jsonBody;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.networkResponse.statusCode == 400) {
            Toasty.error(context, context.getResources().getString(R.string.invalidCredentials), Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG,error.getMessage());
            Toasty.error(context, context.getResources().getString(R.string.failedToLogin), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Object response) {
        try {
            jsonResponse = new JSONObject(response.toString());
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }

        setSharedPreference(jsonResponse);
        listener.onLoginSuccess(createCustomer(jsonResponse));
}

    private void setSharedPreference(JSONObject json){
        accesTokenEdit.putString("token", json.optString("token"));
        accesTokenEdit.putString("email", json.optString("email"));
        accesTokenEdit.commit();
    }

    private Customer createCustomer(JSONObject json){
        Customer c = new Customer(json.optInt("ID"),
                json.optString("firstname"),
                json.optString("lastname"),
                json.optString("email"));
        return c;
    }

    //call back interface
    public interface OnLoginSuccess {
        void onLoginSuccess(Customer customer);
    }
}
