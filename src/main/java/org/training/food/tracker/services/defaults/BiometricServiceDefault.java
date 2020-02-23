package org.training.food.tracker.services.defaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.training.food.tracker.model.Biometrics;
import org.training.food.tracker.model.User;
import org.training.food.tracker.repo.BiometricRepo;
import org.training.food.tracker.services.BiometricService;

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

    @Transactional
    @Override public Biometrics findByOwner(User owner) {
        return biometricRepo.findByOwner(owner);
    }

    @Override public Biometrics update(Biometrics biometrics) {
        return biometricRepo.update(
                biometrics.getId(),
                biometrics.getAge(),
                biometrics.getSex().toString(),
                biometrics.getWeight(),
                biometrics.getHeight(),
                biometrics.getLifestyle().toString()
        );
    }

    @Override public void delete(Biometrics biometrics) {
        biometricRepo.delete(biometrics);
    }
}
