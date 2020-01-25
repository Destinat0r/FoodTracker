package org.training.food_tracker.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.training.food_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE users SET full_name = ?1, national_name = ?2, age = ?3, weight = ?4, height = ?5, sex = ?6, "
                   + "lifestyle = ?7 WHERE email = ?8", nativeQuery = true)
    Optional<User> updateUserByEmail(String fullName, String nationalName,  BigDecimal age, BigDecimal weight,
            BigDecimal height, String sex, String lifestyle, String email);
}
