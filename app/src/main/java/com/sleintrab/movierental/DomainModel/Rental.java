package com.sleintrab.movierental.DomainModel;

/**
 * Created by barti on 17-Jun-17.
 */

public class Rental {

    private Movie movie;
    private String returnDate;
    private int inventoryID;
    private boolean active;

    public Rental(Movie movie, String returnDate, int inventoryID) {
        this.movie = movie;
        this.returnDate = returnDate;
        this.inventoryID = inventoryID;
    }

    public Rental(Movie movie, int inventoryID) {
        this.movie = movie;
        this.inventoryID = inventoryID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
