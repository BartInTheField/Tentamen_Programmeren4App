package com.sleintrab.movierental.PresentationLayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentedListAdapter extends ArrayAdapter<Movie> {


    public RentedListAdapter(Context context, ArrayList<Movie> movieList){
        super(context, R.layout.rented_list_item, movieList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater productInflater = LayoutInflater.from(getContext());

        View customView = productInflater.inflate(R.layout.movie_list_item, parent, false);

        Movie movie = getItem(position);

        TextView movieTitle = (TextView)customView.findViewById(R.id.movie_title);
        TextView movieYear = (TextView)customView.findViewById(R.id.movie_year);
        TextView movieRating = (TextView)customView.findViewById(R.id.movie_rating);

        movieTitle.setText(movie.getTitle());
        movieYear.setText(String.valueOf(movie.getReleaseYear()));
        movieRating.setText(movie.getRating());
        return customView;
    }
}