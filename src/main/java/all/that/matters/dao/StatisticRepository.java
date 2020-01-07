package all.that.matters.dao;

import all.that.matters.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    List<Statistic> findAllByDateTime_DayOfYear(int dayOfYear);
}
