package com.sleintrab.movierental.Exceptions;

/**
 * Created by Niels on 6/13/2017.
 */

public class PasswordsDontMatchException extends Exception {

    public PasswordsDontMatchException(String message){
        super(message);
    }
}
