package com.sleintrab.movierental.PresentationLayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sleintrab.movierental.API.Login;
import com.sleintrab.movierental.DomainModel.Customer;
import com.sleintrab.movierental.R;

public class LoginActivity extends AppCompatActivity implements Login.OnLoginSuccess {

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.editMail);
        password = (EditText) findViewById(R.id.editPassword);
    }

    public void LoginButton(View v){
        new Login(getApplicationContext(), this).loginAccount(email.getText().toString(),
                password.getText().toString());
    }

    public void registerButton(View v){
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginSuccess(Customer customer) {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("CUSTOMER", customer);
        startActivity(i);
    }
}
