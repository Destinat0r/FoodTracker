package all.that.matters.dao;

import all.that.matters.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT id, user_id, food_id, amount, action, timestamp FROM events WHERE user_id = ?1"
                           + " AND date_trunc('day', timestamp) >= current_date AND action = 'CONSUME'", nativeQuery = true)
    List<Event> findAllConsumedFromTodayByUserId(Long userId);

    List<Event> findAllByUserId(Long userId);
}
