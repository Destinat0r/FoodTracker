package org.training.food.tracker.repo.exceptions;

import org.training.food.tracker.model.User;

import java.util.function.Supplier;

public class UserNotFoundException extends RepoException implements Supplier<User> {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    @Override public User get() {
        return null;
    }
}
