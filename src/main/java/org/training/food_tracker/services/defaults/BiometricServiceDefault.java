package org.training.food_tracker.services.defaults;

import org.training.food_tracker.dto.UserDTO;
import org.training.food_tracker.model.Sex;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.BiometricRepo;
import org.training.food_tracker.model.Biometrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.food_tracker.services.BiometricService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BiometricServiceDefault implements BiometricService {
    private BiometricRepo biometricRepo;

    @Autowired
    public BiometricServiceDefault(BiometricRepo biometricRepo) {
        this.biometricRepo = biometricRepo;
    }

    public void create(Biometrics biometrics) {
        biometricRepo.save(biometrics);
    }

    @Override public Optional<Biometrics> findById(Long id) {
        return biometricRepo.findById(id);
    }

    @Override public Biometrics findByOwner(User owner) {
        return biometricRepo.findByOwner(owner);
    }

    @Override public Biometrics update(Biometrics biometrics) {
        return biometricRepo.update(biometrics);
    }

    @Override public void delete(Biometrics biometrics) {
        biometricRepo.delete(biometrics);
    }
}
