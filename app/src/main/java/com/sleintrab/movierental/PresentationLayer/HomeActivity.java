package com.sleintrab.movierental.PresentationLayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sleintrab.movierental.R;

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().add(R.id.FragmentLayout, new HireFragment()).commit();

    }

    public void openFragment(Fragment fragment){
        if (findViewById(R.id.FragmentLayout) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentLayout, new HireFragment()).commit();

        }
    }
}
