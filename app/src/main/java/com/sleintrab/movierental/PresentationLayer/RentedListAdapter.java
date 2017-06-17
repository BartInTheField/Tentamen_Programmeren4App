package com.sleintrab.movierental.PresentationLayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.DomainModel.Rental;
import com.sleintrab.movierental.R;

import java.util.ArrayList;

/**
 * Created by Niels on 6/15/2017.
 */

public class RentedListAdapter extends ArrayAdapter<Rental> {


    public RentedListAdapter(Context context, ArrayList<Rental> rentalList){
        super(context, R.layout.rented_list_item, rentalList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater productInflater = LayoutInflater.from(getContext());

        View customView = productInflater.inflate(R.layout.rented_list_item, parent, false);

        Rental rental = getItem(position);

        TextView movieTitle = (TextView)customView.findViewById(R.id.movie_title);
        TextView movieYear = (TextView)customView.findViewById(R.id.movie_year);
        TextView movieRating = (TextView)customView.findViewById(R.id.movie_rating);
        TextView rentalReturn = (TextView)customView.findViewById(R.id.movie_return_date);

        movieTitle.setText(rental.getMovie().getTitle());
        movieYear.setText(String.valueOf(rental.getMovie().getReleaseYear()));
        movieRating.setText(rental.getMovie().getRating());
        rentalReturn.setText(rental.getReturnDate());

        return customView;
    }
}