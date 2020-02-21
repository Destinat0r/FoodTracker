package org.training.food_tracker.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.training.food_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value = "UPDATE users SET username = ?2, "
                                  + "password = ?3, "
                                  + "first_name = ?4, "
                                  + "last_name = ?5, "
                                  + "email = ?6, "
                                  + "role = ?7, "
                                  + "daily_norm_calories = ?8 WHERE id = ?1", nativeQuery = true)
    User updateById(Long id, String username, String password, String firstName, String lastName, String email,
            String role, BigDecimal dailyNormCalories);


}
