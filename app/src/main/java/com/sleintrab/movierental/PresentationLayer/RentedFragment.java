package com.sleintrab.movierental.PresentationLayer;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.sleintrab.movierental.API.RentalAPI;
import com.sleintrab.movierental.DomainModel.Customer;
import com.sleintrab.movierental.DomainModel.Rental;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentedFragment extends Fragment implements RentalAPI.OnRentalSuccess, RentalAPI.OnRentalsAvailable , RentalAPI.OnRentalFailed{

    private final String TAG = getClass().getSimpleName();
    private ListView rentedListView;
    private RentedListAdapter rentedListAdapter;
    private RentalAPI rentalAPI;
    private FrameLayout spinner;
    private ArrayList<Rental> rentals;
    private AlertDialog dialog;
    private Customer customer;
    private ProgressDialog pd;
    private Boolean pdIsVisible = false;
    private Boolean loadingRentals = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.rented_fragment_view,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customer = ((HomeActivity) getActivity()).getCustomer();
        rentedListView = (ListView)getView().findViewById(R.id.rented_movie_listView);
        loadRentals();
    }

    private void loadRentals(){
        rentalAPI = new RentalAPI(getActivity().getApplicationContext(), this, this, this);
        loadingRentals = true;

        spinner = (FrameLayout) getView().findViewById(R.id.loadingLayout);
        spinner.setVisibility(View.VISIBLE);

        try {
            rentalAPI.getRentals(customer.getId());
        } catch (AuthFailureError authFailureError) {
            Log.e(TAG,authFailureError.getMessage());
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void setOnItemClick(){

        rentedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to return this movie?");
                builder.setTitle("RETURN '" + rentals.get(position).getMovie().getTitle() + "'");
                builder.setPositiveButton("Yes, return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doRentalAPIHandIn(position);
                    }
                });
                builder.setNegativeButton("No, keep it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void doRentalAPIHandIn(int rentalPosition){
        try {
            new RentalAPI(getContext(), this,this,this).handInRental(customer.getId(),
                    rentals.get(rentalPosition).getInventoryID());
        } catch (AuthFailureError authFailureError) {
            Log.e(TAG,authFailureError.getMessage());
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        showProgressDialog();
    }

    private void showProgressDialog(){
        pdIsVisible = true;
        pd = new ProgressDialog(getContext());
        pd.setMessage("Returning movie...");
        pd.show();
    }

    @Override
    public void onRentalSuccess() {
        pdIsVisible = false;
        pd.cancel();
        dialog.dismiss();
        loadRentals();
    }

    @Override
    public void onRentalsAvailable(ArrayList<Rental> rentals) {
        this.rentals = rentals;
        rentedListAdapter = new RentedListAdapter(getActivity().getApplicationContext(), rentals);
        rentedListView.setAdapter(rentedListAdapter);

        setOnItemClick();

        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onRentalFailed() {
      if (pdIsVisible){
            spinner.setVisibility(View.GONE);
            pd.cancel();
            Toasty.error(getContext(), "Error occurred while handing in movie, please try again.", Toast.LENGTH_SHORT).show();
        } else if (loadingRentals){
            loadingRentals = false;
            spinner.setVisibility(View.GONE);
            FrameLayout noRented = (FrameLayout) getView().findViewById(R.id.no_rentedLayout);
            noRented.setVisibility(View.VISIBLE);
        }
    }

}

