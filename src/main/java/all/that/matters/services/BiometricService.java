package all.that.matters.services;

import all.that.matters.dto.UserDTO;
import all.that.matters.model.User;
import all.that.matters.repo.BiometricRepository;
import all.that.matters.model.Biometrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
