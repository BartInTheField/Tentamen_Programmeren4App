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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.sleintrab.movierental.API.MovieAPI;
import com.sleintrab.movierental.API.RentalAPI;
import com.sleintrab.movierental.DomainModel.Customer;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentedFragment extends Fragment implements MovieAPI.OnMoviesAvailable, MovieAPI.NoMoviesAvailable, RentalAPI.OnRentalSuccess, RentalAPI.OnRentalFailed {

    private ListView rentedListView;
    private RentedListAdapter rentedListAdapter;
    private MovieAPI movieAPI;
    private FrameLayout spinner;
    private ArrayList<Movie> movies;
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
        loadMovies();
    }

    private void loadMovies(){
        movieAPI = new MovieAPI(getActivity().getApplicationContext(), this, this,
                customer.getId());

        spinner = (FrameLayout) getView().findViewById(R.id.loadingLayout);
        spinner.setVisibility(View.VISIBLE);

        try {
            movieAPI.retrieveMovies();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    @Override
    public void onMoviesAvailable(ArrayList<Movie> movies) {
        this.movies = movies;
        rentedListAdapter = new RentedListAdapter(getActivity().getApplicationContext(), movies);
        rentedListView.setAdapter(rentedListAdapter);

        setOnItemClick();

        spinner.setVisibility(View.GONE);
    }

    private void setOnItemClick(){

        rentedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to hand in this movie?");
                builder.setTitle("HAND IN '" + movies.get(position).getTitle() + "'");
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

    private void doRentalAPIHandIn(int moviePosition){
        new RentalAPI(getContext(), this,this).handInRental(customer.getId(),
                movies.get(moviePosition).getInventoryID());
        showProgressDialog();

    }

    private void showProgressDialog(){
        pd = new ProgressDialog(getContext());
        pd.setMessage("Handing in movie...");
        pd.show();
    }

    @Override
    public void noMoviesAvailable() {
        FrameLayout frameLayout = (FrameLayout) getView().findViewById(R.id.no_rentedLayout);
        frameLayout.setVisibility(View.VISIBLE);

        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onRentalSuccess() {
        pd.cancel();
        dialog.dismiss();
        loadMovies();
    }

    @Override
    public void onRentalFailed() {
        pd.cancel();
        Toasty.error(getContext(), "Error occurred while handing in movie, please try again.", Toast.LENGTH_SHORT).show();
    }
}

