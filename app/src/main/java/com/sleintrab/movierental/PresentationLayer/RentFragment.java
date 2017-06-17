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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.sleintrab.movierental.API.CopyAPI;
import com.sleintrab.movierental.API.MovieAPI;
import com.sleintrab.movierental.API.RentalAPI;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.DomainModel.Rental;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentFragment extends Fragment implements MovieAPI.OnMoviesAvailable, CopyAPI.OnCopyAvailable, CopyAPI.NoCopyAvailable, RentalAPI.OnRentalFailed, RentalAPI.OnRentalSuccess, RentalAPI.OnRentalsAvailable {

    private ListView movieListView;
    private MovieListAdapter movieListAdapter;
    private MovieAPI movieAPI;
    private CopyAPI copyAPI;
    private RentalAPI rentalAPI;
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.rent_fragment_view,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieAPI = new MovieAPI(getContext(), this);
        copyAPI = new CopyAPI(getContext(),this,this);
        rentalAPI = new RentalAPI(getContext(), this,this, this);
        movieListView = (ListView)getView().findViewById(R.id.rent_movie_listView);

        try {
            movieAPI.retrieveMovies();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    @Override
    public void onMoviesAvailable(final ArrayList<Movie> movies) {
        movieListAdapter = new MovieListAdapter(getActivity().getApplicationContext(), movies);

        ProgressBar spinner = (ProgressBar)getView().findViewById(R.id.loadingBar);
        spinner.setVisibility(View.GONE);

        movieListView.setAdapter(movieListAdapter);
        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie)movieListView.getItemAtPosition(position);
                Log.i("OnMovieClick", String.valueOf(movie.getID()));
                copyAPI.retrieveCopies(movie.getID());
            }
        });
    }

    public void createRentDialog(final int copyID){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.confirmRentMessage));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("ConfirmRent", "Rented film");

                try {
                    rentalAPI.makeRental(((HomeActivity)getActivity()).getCustomer().getId(), copyID);
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
        pd = new ProgressDialog(getContext());
        pd.setMessage("Renting movie...");
        pd.show();
    }

    @Override
    public void onCopyAvailable(int copyID) {
        Log.i("OnCopyAvailable", "Copy available: " + copyID);
        createRentDialog(copyID);
    }

    @Override
    public void noCopyAvailable() {
        Log.i("OnCopyAvailable", "No copies available");
        Toasty.error(getContext(), "There are no copies available of this movie.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRentalSuccess() {
        pd.cancel();
        Toasty.success(getContext(), "Successfully rented movie!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRentalFailed() {
        pd.cancel();
        Toasty.error(getContext(), "Error occurred while renting movie, please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRentalsAvailable(ArrayList<Rental> rentals) {

    }
}
