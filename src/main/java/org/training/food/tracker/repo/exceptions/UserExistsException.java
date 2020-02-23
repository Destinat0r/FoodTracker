package org.training.food.tracker.repo.exceptions;

public class UserExistsException extends Exception {

    public UserExistsException() {
    }

    public UserExistsException(String message) {
        super(message);
    }
}
