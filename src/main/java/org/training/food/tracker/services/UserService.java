package org.training.food.tracker.services;

import org.springframework.stereotype.Service;
import org.training.food.tracker.model.Biometrics;
import org.training.food.tracker.model.User;
import org.training.food.tracker.repo.exceptions.UserExistsException;
import org.training.food.tracker.repo.exceptions.UserNotFoundException;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface UserService {
    User create(User user) throws UserExistsException;

    User findById(Long id) throws UserNotFoundException;

    User findByUsername(String username) throws UserNotFoundException;

    List<User> findAll();

    int update(User user);

    BigDecimal calculateDailyNormCalories(Biometrics biometrics);
}
