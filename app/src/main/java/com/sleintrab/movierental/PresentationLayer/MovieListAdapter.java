package com.sleintrab.movierental.PresentationLayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.sleintrab.movierental.R;

import com.sleintrab.movierental.DomainModel.Movie;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Niels on 6/15/2017.
 */

public class MovieListAdapter extends ArrayAdapter<Movie> implements
        SectionIndexer {

    HashMap<String, Integer> sectionsMap = new HashMap<String, Integer>();
    ArrayList<String> sectionsList = new ArrayList<String>();

    ArrayList<Integer> sectionForPosition = new ArrayList<Integer>();
    ArrayList<Integer> positionForSection = new ArrayList<Integer>();


    public MovieListAdapter(Context context, ArrayList<Movie> movieList){
        super(context, R.layout.movie_list_item, movieList);


        // Figure out what the sections should be (one section per unique
        // initial letter, to accommodate letters that might be missing,
        // or characters like ,)
        for (int i = 0; i < movieList.size(); i++) {
            String objectString = movieList.get(i).getTitle();
            if (objectString.length() > 0) {
                String firstLetter = objectString.substring(0, 1).toUpperCase();
                if (!sectionsMap.containsKey(firstLetter)) {
                    sectionsMap.put(firstLetter, sectionsMap.size());
                    sectionsList.add(firstLetter);
                }
            }
        }

        // Calculate the section for each position in the list.
        for (int i = 0; i < movieList.size(); i++) {
            String objectString = movieList.get(i).getTitle();
            if (objectString.length() > 0) {
                String firstLetter = objectString.substring(0, 1).toUpperCase();
                if (sectionsMap.containsKey(firstLetter)) {
                    sectionForPosition.add(sectionsMap.get(firstLetter));
                } else
                    sectionForPosition.add(0);
            } else
                sectionForPosition.add(0);
        }

        // Calculate the first position where each section begins.
        for (int i = 0; i < sectionsMap.size(); i++)
            positionForSection.add(0);
        for (int i = 0; i < sectionsMap.size(); i++) {
            for (int j = 0; j < movieList.size(); j++) {
                Integer section = sectionForPosition.get(j);
                if (section == i) {
                    positionForSection.set(i, j);
                    break;
                }
            }
        }
    }

    public int getPositionForSection(int section) {
        return positionForSection.get(section);
    }

    public int getSectionForPosition(int position) {
        return sectionForPosition.get(position);
    }

    public Object[] getSections() {
        return sectionsList.toArray();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater productInflater = LayoutInflater.from(getContext());

        View customView = productInflater.inflate(R.layout.movie_list_item, parent, false);

        Movie movie = getItem(position);

        TextView movieTitle = (TextView)customView.findViewById(R.id.movie_title);
        TextView movieYear = (TextView)customView.findViewById(R.id.movie_year);
        TextView movieRating = (TextView)customView.findViewById(R.id.movie_rating);
        TextView moviePrice = (TextView)customView.findViewById(R.id.movie_price);

        movieTitle.setText(movie.getTitle());
        movieYear.setText(String.valueOf(movie.getReleaseYear()));
        movieRating.setText(movie.getRating());
        moviePrice.setText(String.format("€%.2f", movie.getPrice()));
        return customView;
    }
}