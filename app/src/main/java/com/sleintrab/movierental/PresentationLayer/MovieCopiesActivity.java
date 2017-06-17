package com.sleintrab.movierental.PresentationLayer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    private CopyAPI copyAPI;
    private RentalAPI rentalAPI;
    private ProgressDialog pd;
    private Movie movie;
    private Customer customer;
    private ListView copyListView;
    private CopyListAdapter copyListAdapter;
    private ArrayList<Copy> copies = new ArrayList<>();
    private ArrayList<Integer> inventoryIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_copies);

        customer = (Customer)getIntent().getSerializableExtra("customer");
        movie = (Movie)getIntent().getSerializableExtra("movie");

        copyAPI = new CopyAPI(getApplicationContext(),this,this);
        rentalAPI = new RentalAPI(getApplicationContext(), this, this, this, this);

        copyListView = (ListView)findViewById(R.id.rent_movie_listView);


        try {
            rentalAPI.getActiveRentals();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        copyAPI.retrieveCopies(movie.getID());
    }

    public void createRentDialog(final int inventoryID){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.confirmRentMessage));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    rentalAPI.makeRental(customer.getId(), inventoryID);
                } catch (AuthFailureError authFailureError) {
                    authFailureError.printStackTrace();
                }

                showProgressDialog();
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

    private void showProgressDialog(){
        pd = new ProgressDialog(this);
        pd.setMessage("Renting movie...");
        pd.show();
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
                    createRentDialog(rental.getInventoryID());
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
        pd.cancel();
        Toasty.success(getApplicationContext(), "Successfully rented movie!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRentalFailed() {
        pd.cancel();
        Toasty.error(getApplicationContext(), "Error occurred while renting movie, please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRentalsAvailable(ArrayList<Rental> rentals) {

    }

    @Override
    public void onActiveRentalsAvailable(ArrayList<Integer> inventoryIDs) {
        this.inventoryIDs = inventoryIDs;
    }
}
