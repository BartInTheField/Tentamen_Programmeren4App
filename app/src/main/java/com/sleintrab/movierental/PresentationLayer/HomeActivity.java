package com.sleintrab.movierental.PresentationLayer;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sleintrab.movierental.R;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends FragmentActivity {

    private ConstraintLayout footer;
    private ImageView filmsButton;
    private ImageView rentedButton;
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        footer = (ConstraintLayout)findViewById(R.id.footer);
        filmsButton = (ImageView)footer.findViewById(R.id.home_movies);
        rentedButton = (ImageView)footer.findViewById(R.id.home_rentedMovies);


        openFragment(new RentFragment());

        filmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFragment instanceof RentedFragment){
                    openFragment(new RentFragment());
                }
            }
        });

        rentedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFragment instanceof RentFragment){
                    openFragment(new RentedFragment());
                }
            }
        });
    }

    public void openFragment(Fragment fragment){
        if (findViewById(R.id.FragmentLayout) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentLayout, fragment).commit();
            selectedFragment = fragment;
            if(selectedFragment instanceof RentFragment){
                rentedButton.setColorFilter(ContextCompat.getColor(HomeActivity.this,R.color.white));
                filmsButton.setColorFilter(ContextCompat.getColor(HomeActivity.this,R.color.colorAccent));
            }else{
                filmsButton.setColorFilter(ContextCompat.getColor(HomeActivity.this,R.color.white));
                rentedButton.setColorFilter(ContextCompat.getColor(HomeActivity.this,R.color.colorAccent));
            }
        }
    }
}
