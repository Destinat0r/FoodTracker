package org.training.food_tracker.repo.exceptions;

import org.training.food_tracker.model.Day;

import java.util.function.Supplier;

public class DayNotFoundException extends RepoException implements Supplier<Day> {

    public DayNotFoundException() {
    }

    public DayNotFoundException(String message) {
        super(message);
    }

    @Override public Day get() {
        return null;
    }
}
