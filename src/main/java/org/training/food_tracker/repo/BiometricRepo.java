package org.training.food_tracker.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.training.food_tracker.model.Biometrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BiometricRepo extends JpaRepository<Biometrics, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE biometrics SET age = ?2, sex = ?3, weight = ?4, height = ?5, lifestyle = ?6, norm = ?7 "
                           + "WHERE user_id = ?1", nativeQuery = true)
    int updateByOwnerId(Long id, BigDecimal age, String sex, BigDecimal weight, BigDecimal height, String lifestyle,
            BigDecimal dailyNorm);
}
