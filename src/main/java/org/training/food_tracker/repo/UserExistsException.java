package org.training.food_tracker.repo;

public class UserExistsException extends Exception {

    public UserExistsException() {
    }

    public UserExistsException(String message) {
        super(message);
    }
}
