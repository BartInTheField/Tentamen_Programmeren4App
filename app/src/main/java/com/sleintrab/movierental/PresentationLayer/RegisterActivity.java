package com.sleintrab.movierental.PresentationLayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sleintrab.movierental.Exceptions.EmptyFieldException;
import com.sleintrab.movierental.Exceptions.PasswordsDontMatchException;
import com.sleintrab.movierental.API.RegisterAPI;
import com.sleintrab.movierental.R;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements RegisterAPI.OnRegisterSuccess {

    private final String TAG = getClass().getSimpleName();

    private EditText firstName, lastName, email, password, confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = (EditText)findViewById(R.id.register_edtFirstName);
        lastName = (EditText)findViewById(R.id.register_edtLastName);
        email = (EditText)findViewById(R.id.register_edtEmail);
        password = (EditText)findViewById(R.id.register_edtPassword);
        confirmPassword = (EditText)findViewById(R.id.register_edtConfirmPassword);

    }

    public void registerButton(View v){
        try{
            new RegisterAPI(getApplicationContext(), this).RegisterCustomer(firstName.getText().toString(),
                    lastName.getText().toString(), email.getText().toString(),password.getText().toString(),
                    confirmPassword.getText().toString());
        }catch(EmptyFieldException e){
            Log.e(TAG,e.getMessage());
            Toasty.error(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }catch(PasswordsDontMatchException e){
            Log.e(TAG,e.getMessage());
            Toasty.error(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegisterSuccess() {

        Toasty.success(getApplicationContext(), "Succesfully created a customer.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
