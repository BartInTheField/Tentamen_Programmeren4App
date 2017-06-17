package com.sleintrab.movierental.DomainModel;

/**
 * Created by barti on 17-Jun-17.
 */

public class Rental {

    private Movie movie;
    private String returnDate;
    private int inventoryID;

    public Rental(Movie movie, String returnDate, int inventoryID) {
        this.movie = movie;
        this.returnDate = returnDate;
        this.inventoryID = inventoryID;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }
}
