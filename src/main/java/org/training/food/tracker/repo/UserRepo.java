package org.training.food.tracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.training.food.tracker.model.User;

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
    int update(Long id, String username, String password, String firstName, String lastName, String email,
            String role, BigDecimal dailyNormCalories);


}
