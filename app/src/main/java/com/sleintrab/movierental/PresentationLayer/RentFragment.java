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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.sleintrab.movierental.API.CopyAPI;
import com.sleintrab.movierental.API.MovieAPI;
import com.sleintrab.movierental.API.RentalAPI;
import com.sleintrab.movierental.DomainModel.Copy;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.DomainModel.Rental;
import com.sleintrab.movierental.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentFragment extends Fragment implements MovieAPI.OnMoviesAvailable{

    private ListView movieListView;
    private MovieListAdapter movieListAdapter;
    private MovieAPI movieAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.rent_fragment_view,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieAPI = new MovieAPI(getContext(), this);
        movieListView = (ListView)getView().findViewById(R.id.rent_movie_listView);

        try {
            movieAPI.retrieveMovies();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
                Intent i = new Intent(getContext(),MovieCopiesActivity.class);
                i.putExtra("movie",movie);
                i.putExtra("customer", ((HomeActivity)getActivity()).getCustomer());
                startActivity(i);
            }
        });

        movieListView.requestFocus();
    }
}
