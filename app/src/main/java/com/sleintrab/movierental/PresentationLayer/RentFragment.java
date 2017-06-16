package com.sleintrab.movierental.PresentationLayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.sleintrab.movierental.API.MovieAPI;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentFragment extends Fragment implements MovieAPI.OnMoviesAvailable {

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
        movieAPI = new MovieAPI(getActivity().getApplicationContext(), this);
        movieListView = (ListView)getView().findViewById(R.id.rent_movie_listView);

        try {
            movieAPI.retrieveMovies();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    @Override
    public void onMoviesAvailable(ArrayList<Movie> movies) {
        movieListAdapter = new MovieListAdapter(getActivity().getApplicationContext(), movies);

        ProgressBar spinner = (ProgressBar)getView().findViewById(R.id.loadingBar);
        spinner.setVisibility(View.GONE);

        movieListView.setAdapter(movieListAdapter);

    }
}
