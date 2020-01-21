package org.training.food_tracker.repo;

import org.training.food_tracker.model.User;

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
