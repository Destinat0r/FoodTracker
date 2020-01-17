package all.that.matters.repo;

import all.that.matters.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository public interface EventRepo extends JpaRepository<Event, Long> {

    @Query(value = "SELECT id, user_id, food, amount, total_calories, timestamp FROM events WHERE user_id = ?1"
                           + " AND date_trunc('day', timestamp) >= current_date ORDER BY timestamp DESC", nativeQuery = true)
    List<Event> findAllConsumedFromTodayByUserId(Long userId);

    List<Event> findAllByUserId(Long userId);

    @Query(value = "SELECT SUM(total_calories) FROM events WHERE user_id = ?1 AND date_trunc('day', timestamp) = ?2",
            nativeQuery = true)
    Optional<BigDecimal> getTotalConsumedCaloriesByUserIdAndDate(Long userId, LocalDate date);
}
