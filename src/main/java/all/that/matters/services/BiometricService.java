package all.that.matters.services;

import all.that.matters.dao.BiometricRepository;
import all.that.matters.domain.Biometrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
