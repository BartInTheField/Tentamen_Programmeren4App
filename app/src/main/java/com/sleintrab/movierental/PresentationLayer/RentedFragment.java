package com.sleintrab.movierental.PresentationLayer;


import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.sleintrab.movierental.API.MovieAPI;
import com.sleintrab.movierental.API.RentalAPI;
import com.sleintrab.movierental.DomainModel.Customer;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.DomainModel.Rental;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentedFragment extends Fragment implements RentalAPI.OnRentalSuccess, RentalAPI.OnRentalsAvailable{

    private ListView rentedListView;
    private RentedListAdapter rentedListAdapter;
    private RentalAPI rentalAPI;
    private FrameLayout spinner;
    private ArrayList<Rental> rentals;
    private AlertDialog dialog;
    private Customer customer;
    private ProgressDialog pd;

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
        rentalAPI = new RentalAPI(getActivity().getApplicationContext(), this, this);

        spinner = (FrameLayout) getView().findViewById(R.id.loadingLayout);
        spinner.setVisibility(View.VISIBLE);

        try {
            rentalAPI.getRentals(customer.getId());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    private void setOnItemClick(){

        rentedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to hand in this movie?");
                builder.setTitle("HAND IN '" + rentals.get(position).getMovie().getTitle() + "'");
                builder.setPositiveButton("Yes, hand in", new DialogInterface.OnClickListener() {
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
            new RentalAPI(getContext(), this,this).handInRental(customer.getId(),
                    rentals.get(rentalPosition).getInventoryID());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        showProgressDialog();

    }

    private void showProgressDialog(){
        pd = new ProgressDialog(getContext());
        pd.setMessage("Handing in movie...");
        pd.show();
    }

    @Override
    public void onRentalSuccess() {
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
}

