package com.sleintrab.movierental.PresentationLayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.sleintrab.movierental.API.MovieAPI;
import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentedFragment extends Fragment implements MovieAPI.OnMoviesAvailable {

    private ListView rentedListView;
    private RentedListAdapter rentedListAdapter;
    private MovieAPI movieAPI;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.rented_fragment_view,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieAPI = new MovieAPI(getActivity().getApplicationContext(), this,
                ((HomeActivity) getActivity()).getCustomer().getId());
        rentedListView = (ListView)getView().findViewById(R.id.rented_movie_listView);

        try {
            movieAPI.retrieveMovies();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    @Override
    public void onMoviesAvailable(ArrayList<Movie> movies) {
        rentedListAdapter = new RentedListAdapter(getActivity().getApplicationContext(), movies);
        rentedListView.setAdapter(rentedListAdapter);


        FrameLayout frameLayout = (FrameLayout) getView().findViewById(R.id.no_rentedLayout);
        frameLayout.setVisibility(View.GONE);
    }
}

