package com.sleintrab.movierental.PresentationLayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentedFragment extends Fragment {

    private ListView rentedListView;
    private RentedListAdapter rentedListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.rented_fragment_view,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Movie> movies = new ArrayList<>();
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
//        movies.add(new Movie(1, "Rented", "nwwwe", 1994, 8, 2.99, 120, 29.99, "GDK", "Felix die op een wip zit"));
        rentedListView = (ListView)getView().findViewById(R.id.rented_movie_listView);

        if (!movies.isEmpty()) {
            FrameLayout frameLayout = (FrameLayout) getView().findViewById(R.id.no_rentedLayout);
            frameLayout.setVisibility(View.GONE);
            rentedListAdapter = new RentedListAdapter(getActivity().getApplicationContext(), movies);
            rentedListView.setAdapter(rentedListAdapter);
        }
    }
}

