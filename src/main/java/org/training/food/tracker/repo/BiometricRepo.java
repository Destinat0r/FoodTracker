package org.training.food.tracker.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.training.food.tracker.model.User;
import org.training.food.tracker.model.Biometrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BiometricRepo extends JpaRepository<Biometrics, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE biometrics SET age = ?2, sex = ?3, weight = ?4, height = ?5, lifestyle = ?6 "
                           + "WHERE user_id = ?1", nativeQuery = true)
    int updateByOwnerId(Long id, BigDecimal age, String sex, BigDecimal weight, BigDecimal height, String lifestyle);

    Optional<Biometrics> findById(Long id);

    Biometrics findByOwner(User owner);

    Biometrics update(Biometrics biometrics);

    void delete(Biometrics biometrics);
}
