package org.training.food_tracker.repo.exceptions;

public class FoodNotFoundException extends RepoException {

    public FoodNotFoundException() {
    }

    public FoodNotFoundException(String message) {
        super(message);
    }
}
