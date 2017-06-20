package com.sleintrab.movierental.PresentationLayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sleintrab.movierental.API.LoginAPI;
import com.sleintrab.movierental.DomainModel.Customer;
import com.sleintrab.movierental.R;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements LoginAPI.OnLoginSuccess, LoginAPI.OnLoginFailed{

    private EditText email, password;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.editMail);
        password = (EditText) findViewById(R.id.editPassword);
    }

    public void LoginButton(View v){
        new LoginAPI(getApplicationContext(), this,this).loginAccount(email.getText().toString(),
                password.getText().toString());
        showProgressDialog();
    }

    private void showProgressDialog(){
        pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.loggingIn));
        pd.show();
    }

    public void registerButton(View v){
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginSuccess(Customer customer) {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("CUSTOMER", customer);
        pd.cancel();
        startActivity(i);
    }

    @Override
    public void onLoginFailed() {
        pd.cancel();
        Toasty.error(getApplicationContext(), getResources().getString(R.string.invalidCredentials), Toast.LENGTH_SHORT).show();
    }
}
