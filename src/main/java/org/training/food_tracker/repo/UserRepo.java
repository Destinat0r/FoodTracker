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

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET full_name = ?2, national_name = ?3, email = ?4, password = ?5 WHERE id = ?1",
            nativeQuery = true)
    int updateById(Long id, String fullName, String nationalName, String email, String password);
}
