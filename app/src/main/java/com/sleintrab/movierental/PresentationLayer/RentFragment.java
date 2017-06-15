package com.sleintrab.movierental.PresentationLayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentFragment extends Fragment {

    private ListView movieListView;
    private MovieListAdapter movieListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.rent_fragment_view,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movies.add(new Movie(1, "nop", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        movieListView = (ListView)getView().findViewById(R.id.rent_movie_listView);
        movieListAdapter = new MovieListAdapter(getActivity().getApplicationContext(), movies);
        movieListView.setAdapter(movieListAdapter);
    }
}
