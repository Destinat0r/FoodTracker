package org.training.food_tracker.services;

import org.training.food_tracker.model.Biometrics;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.exceptions.UserExistsException;
import org.training.food_tracker.repo.exceptions.UserNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User create(User user) throws UserExistsException;

    User findById(Long id) throws UserNotFoundException;

    User findByUsername(String username) throws UserNotFoundException;

    List<User> findAll();

    User update(User user);

    BigDecimal calculateDailyNormCalories(Biometrics biometrics);
}
