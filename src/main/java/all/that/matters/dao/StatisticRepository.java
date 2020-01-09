package all.that.matters.dao;

import all.that.matters.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query(value = "SELECT id, user_id, food_id, amount, action, timestamp FROM statistics WHERE user_id = ?1"
                           + " AND date_trunc('day', timestamp) >= current_date AND action = 'CONSUME'", nativeQuery = true)
    List<Statistic> findAllConsumedFromTodayByUserId(Long userId);

    List<Statistic> findAllByUserId(Long userId);
}
