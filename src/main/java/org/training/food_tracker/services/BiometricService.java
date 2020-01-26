package org.training.food_tracker.services;

import org.training.food_tracker.dto.UserDTO;
import org.training.food_tracker.model.Lifestyle;
import org.training.food_tracker.model.Sex;
import org.training.food_tracker.repo.BiometricRepository;
import org.training.food_tracker.model.Biometrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BiometricService {
    private BiometricRepository biometricRepository;

    @Autowired
    public BiometricService(BiometricRepository biometricRepository) {
        this.biometricRepository = biometricRepository;
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
                calculateDailyNorm(userDTO));
    }

    private BigDecimal calculateDailyNorm(UserDTO userDTO) {
        if (userDTO.getSex() == Sex.MALE) {
            return (new BigDecimal(66.5)
                            .add(new BigDecimal(13.75).multiply(userDTO.getWeight()))
                            .add(new BigDecimal(5.003).multiply(userDTO.getHeight()))
                            .subtract(new BigDecimal(6.755).multiply(userDTO.getAge())))
                           .multiply(userDTO.getLifestyle().getCoefficient());
        }
        return (new BigDecimal(655.1)
                        .add(new BigDecimal(9.563).multiply(userDTO.getWeight()))
                        .add(new BigDecimal(1.850).multiply(userDTO.getHeight()))
                        .subtract(new BigDecimal(4.676).multiply(userDTO.getAge())))
                       .multiply(userDTO.getLifestyle().getCoefficient());
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
