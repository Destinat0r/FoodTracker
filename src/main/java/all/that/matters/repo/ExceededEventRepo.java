package all.that.matters.repo;

import all.that.matters.model.ExceededEvent;
import all.that.matters.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExceededEventRepo extends JpaRepository<ExceededEvent, Long> {

    @Modifying
    @Query(value = "UPDATE exceed_events SET excessive_calories = ?1 WHERE date = ?2 AND user_id = ?3", nativeQuery = true)
    void updateExcessiveCaloriesByDateAndUserId(BigDecimal calories, LocalDate date, Long userId);

    boolean existsByUserAndDate(User user, LocalDate date);
    List<ExceededEvent> findAllByUser(User user);
}
