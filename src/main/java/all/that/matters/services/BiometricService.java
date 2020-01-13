package all.that.matters.services;

import all.that.matters.repo.BiometricRepository;
import all.that.matters.model.Biometrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
