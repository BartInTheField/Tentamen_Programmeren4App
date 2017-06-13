package com.sleintrab.movierental;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sleintrab.movierental.API.EmptyFieldException;
import com.sleintrab.movierental.API.PasswordsDontMatchException;
import com.sleintrab.movierental.API.Register;

public class RegisterActivity extends AppCompatActivity {

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
            new Register(getApplicationContext()).RegisterCustomer(firstName.getText().toString(),
                    lastName.getText().toString(), email.getText().toString(),password.getText().toString(),
                    confirmPassword.getText().toString());
        }catch(EmptyFieldException e){
            e.printStackTrace();
        }catch(PasswordsDontMatchException e){
            e.printStackTrace();
        }
    }
}
