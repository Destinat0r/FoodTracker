package org.training.food_tracker.services;

import org.training.food_tracker.model.Biometrics;
import org.training.food_tracker.model.User;

import java.util.Optional;

public interface BiometricService {
    void create(Biometrics biometrics);

    Optional<Biometrics> findById(Long id);

    Biometrics findByOwner(User user);

    Biometrics update(Biometrics biometrics);

    void delete(Biometrics biometrics);
}

