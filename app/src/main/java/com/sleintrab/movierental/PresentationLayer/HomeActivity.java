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
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        footer = (ConstraintLayout)findViewById(R.id.footer);
        filmsButton = (ImageView)footer.findViewById(R.id.home_Films);

        openFragment(new RentFragment());

        filmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFragment instanceof RentFragment){
                    openFragment(new RentFragment());
                    Toasty.success(HomeActivity.this, "Geklikt", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openFragment(Fragment fragment){
        if (findViewById(R.id.FragmentLayout) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentLayout, fragment).commit();
            selectedFragment = fragment;
            if(selectedFragment instanceof RentFragment){
                filmsButton.setColorFilter(ContextCompat.getColor(HomeActivity.this,R.color.colorAccent));
            }else{
                //TODO: goede button aangeven
                //filmsButton.setColorFilter(ContextCompat.getColor(HomeActivity.this,R.color.colorAccent));
            }
        }
    }
}