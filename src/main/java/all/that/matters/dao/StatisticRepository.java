package all.that.matters.dao;

import all.that.matters.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query(value = "SELECT id, food, amount, action, date(timestamp) as date FROM statistics WHERE date >= current_date",
    nativeQuery = true)
    List<Statistic> findAllFromToday();
}
