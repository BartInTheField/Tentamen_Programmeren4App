package com.sleintrab.movierental.API;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sleintrab.movierental.HomeActivity;
import com.sleintrab.movierental.LoginActivity;
import com.sleintrab.movierental.Volley.JSONObjectRequest;
import com.sleintrab.movierental.Volley.VolleyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Niels on 6/13/2017.
 */

public class Register implements Response.ErrorListener, Response.Listener{

    private final String URL = "URLFORAPI";

    private JSONObject jsonResponse;
    private RequestQueue mQueue;

    private static Context context;

    public Register(Context context){
        this.context = context;

        mQueue = VolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    public void RegisterCustomer(String firstName, String lastName, String email, String password,
                                 String confirmPassword) throws EmptyFieldException, PasswordsDontMatchException {
        ArrayList<String> fields = new ArrayList<>();
        fields.add(firstName);
        fields.add(lastName);
        fields.add(email);
        fields.add(password);
        fields.add(confirmPassword);

        if(hasEmptyFields(fields)){
            throw new EmptyFieldException("One or more fields are empty");
        }
        if(!passwordsMatch(password,confirmPassword)){
            throw new PasswordsDontMatchException("Passwords have to match");
        }

        final JSONObjectRequest req = new JSONObjectRequest(Request.Method.POST,
                URL,
                makeBodyJSON(firstName, lastName, email, password),
                this,
                this);
        req.setTag("RegisterTAG");
        mQueue.add(req);
    }

    private JSONObject makeBodyJSON(String firstName, String lastName, String email, String password){
        JSONObject jsonBody = new JSONObject();

        try{
            jsonBody.put("firstName", firstName);
            jsonBody.put("lastName", lastName);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonBody;
    }

    private boolean passwordsMatch(String password, String secondPassword){
        if(password.equals(secondPassword)){
            return true;
        }else{
            return false;
        }
    }

    private boolean hasEmptyFields(ArrayList<String> fields) {
        for (int i = 0; i < fields.size(); i++) {
            if(fields.get(i).trim().equalsIgnoreCase("")){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context, "Failed to register customer.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            jsonResponse = new JSONObject(response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(context, LoginActivity.class);
        Toast.makeText(context, "Succesfully created a customer.", Toast.LENGTH_SHORT).show();
        context.startActivity(i);
    }
}