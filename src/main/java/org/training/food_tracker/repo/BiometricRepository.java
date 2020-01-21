package org.training.food_tracker.repo;

import org.training.food_tracker.model.Biometrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometricRepository extends JpaRepository<Biometrics, Long> {

}
