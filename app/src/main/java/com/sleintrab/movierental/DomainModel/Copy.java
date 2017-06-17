package com.sleintrab.movierental.DomainModel;

import java.io.Serializable;

/**
 * Created by Niels on 6/17/2017.
 */

public class Copy implements Serializable {

    private int inventoryID;
    private int filmID;

    public Copy(int inventoryID, int filmID) {
        this.inventoryID = inventoryID;
        this.filmID = filmID;
    }


    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }

    @Override
    public String toString() {
        return "Copy{" +
                "inventoryID=" + inventoryID +
                ", filmID=" + filmID +
                '}';
    }
}
