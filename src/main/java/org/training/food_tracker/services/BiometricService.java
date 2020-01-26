package org.training.food_tracker.services;

import org.training.food_tracker.dto.UserDTO;
import org.training.food_tracker.repo.BiometricRepository;
import org.training.food_tracker.model.Biometrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BiometricService {
    private BiometricRepository biometricRepository;
    private UserService userService;

    @Autowired
    public BiometricService(BiometricRepository biometricRepository, UserService userService) {
        this.biometricRepository = biometricRepository;
        this.userService = userService;
    }

    public void create(Biometrics biometrics) {
        biometricRepository.save(biometrics);
    }

    public int update(UserDTO userDTO) {
        return biometricRepository.updateByOwnerId(
                userDTO.getId(),
                userDTO.getAge(),
                userDTO.getSex().toString(),
                userDTO.getWeight(),
                userDTO.getHeight(),
                userDTO.getLifestyle().toString(),
                userDTOtoBiometrics(userDTO).calculateDailyNorm());
    }

    public Biometrics userDTOtoBiometrics(UserDTO userDTO) {
        return Biometrics.builder()
                       .age(userDTO.getAge())
                       .weight(userDTO.getWeight())
                       .height(userDTO.getHeight())
                       .sex(userDTO.getSex())
                       .lifestyle(userDTO.getLifestyle())
                       .dailyNorm(userDTO.getDailyNorm())
                       .build();
    }
}
