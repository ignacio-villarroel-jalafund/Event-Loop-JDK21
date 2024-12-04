package org.example.eventloop.core;

public class ErrorHandler {
    public void handleError(Exception e) {
        System.err.println("An error occurred: " + e.getMessage());
        e.printStackTrace();
    }
}
