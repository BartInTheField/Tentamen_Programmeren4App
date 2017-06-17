package com.sleintrab.movierental.PresentationLayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sleintrab.movierental.DomainModel.Movie;
import com.sleintrab.movierental.DomainModel.Rental;
import com.sleintrab.movierental.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

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

        int days = daysTillReturn(rental);
        if (days <= 0){
            rentalReturn.setText("Return today");
        } else {
            rentalReturn.setText("Return in " + days + " days");
        }

        return customView;
    }

    private int daysTillReturn(Rental rental){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int days = 0;
        try {
            Date date = df.parse(rental.getReturnDate().substring(0, 10));
            days = daysBetween(new Date(), date);
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    private int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}