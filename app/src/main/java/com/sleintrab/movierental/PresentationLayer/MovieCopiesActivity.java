package com.sleintrab.movierental.PresentationLayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.sleintrab.movierental.API.CopyAPI;
import com.sleintrab.movierental.API.RentalAPI;
import com.sleintrab.movierental.DomainModel.Copy;
import com.sleintrab.movierental.DomainModel.Customer;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.DomainModel.Rental;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MovieCopiesActivity extends AppCompatActivity implements RentalAPI.OnRentalFailed, RentalAPI.OnRentalSuccess, RentalAPI.OnRentalsAvailable, CopyAPI.OnCopiesAvailable, CopyAPI.NoCopiesAvailable, RentalAPI.OnActiveRentalsAvailable {

    private final String SHAREDACCESTOKEN = "ACCESSTOKEN";
    private SharedPreferences accesToken;
    private SharedPreferences.Editor accesTokenEdit;

    private CopyAPI copyAPI;
    private RentalAPI rentalAPI;
    private ProgressDialog pd;
    private Movie movie;
    private Customer customer;
    private ListView copyListView;
    private CopyListAdapter copyListAdapter;
    private ArrayList<Integer> inventoryIDs = new ArrayList<>();
    private Toolbar toolbar;
    private ImageView logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_copies);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        logoutButton = (ImageView)toolbar.findViewById(R.id.logout);

        customer = (Customer)getIntent().getSerializableExtra("customer");
        movie = (Movie)getIntent().getSerializableExtra("movie");

        copyAPI = new CopyAPI(getApplicationContext(),this,this);
        rentalAPI = new RentalAPI(getApplicationContext(), this, this, this, this);

        copyListView = (ListView)findViewById(R.id.rent_movie_listView);

        accesToken = getApplicationContext().getSharedPreferences(SHAREDACCESTOKEN, Context.MODE_PRIVATE);
        accesTokenEdit = accesToken.edit();


        try {
            rentalAPI.getActiveRentals();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        copyAPI.retrieveCopies(movie.getID());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accesTokenEdit.clear();
                accesTokenEdit.apply();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    public void createRentDialog(final Rental rental){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.confirmRentMessage));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    rentalAPI.makeRental(customer.getId(), rental.getInventoryID());
                    rental.setActive(true);
                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                }

                dialog.cancel();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("ConfirmRent", "Cancelled renting film");
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onCopiesAvailable(ArrayList<Copy> copies) {
        Log.i("OnCopyAvailable", "Copy available: " + copies);
        ArrayList<Rental> rentals = new ArrayList<>();
        for (Copy copy : copies) {
            Rental r = new Rental(movie, copy.getInventoryID());
            for (int i = 0; i < inventoryIDs.size(); i++) {
                if(r.getInventoryID() == inventoryIDs.get(i)){
                    r.setActive(true);
                    break;
                }
            }
            Log.i("TAGGG", String.valueOf(r.isActive()));
            rentals.add(r);
        }
        copyListAdapter = new CopyListAdapter(getApplicationContext(), rentals);

        copyListView.setAdapter(copyListAdapter);
        copyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rental rental = (Rental)copyListView.getItemAtPosition(position);
                Log.i("rental test", String.valueOf(rental.isActive()));
                if(rental.isActive()){
                    Toasty.error(getApplicationContext(), "This copy is already rented", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("Rental ID", "Rental ID " + rental.getInventoryID());
                    createRentDialog(rental);
                }
            }
        });
    }

    @Override
    public void noCopiesAvailable() {
        Log.i("OnCopyAvailable", "No copies available");
        Toasty.error(getApplicationContext(), "There are no copies available of this movie.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRentalSuccess() {
        Toasty.success(getApplicationContext(), "Successfully rented movie!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRentalFailed() {
        Toasty.error(getApplicationContext(), "Error occurred while renting movie, please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRentalsAvailable(ArrayList<Rental> rentals) {

    }

    @Override
    public void onActiveRentalsAvailable(ArrayList<Integer> inventoryIDs) {
        this.inventoryIDs = inventoryIDs;
        for (int i = 0; i < this.inventoryIDs.size(); i++) {
            Log.i("TAGAGA", String.valueOf(this.inventoryIDs.get(i)));
        }
    }
}
