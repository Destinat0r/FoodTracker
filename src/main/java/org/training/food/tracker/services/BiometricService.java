package org.training.food.tracker.services;

import org.springframework.stereotype.Service;
import org.training.food.tracker.model.Biometrics;
import org.training.food.tracker.model.User;

import java.util.Optional;

@Service
public interface BiometricService {
    void create(Biometrics biometrics);

    Optional<Biometrics> findById(Long id);

    Biometrics findByOwner(User user);

    void update(Biometrics biometrics);

    void delete(Biometrics biometrics);
}

