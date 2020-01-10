package all.that.matters.dao;

import all.that.matters.domain.Day;
import all.that.matters.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {


    Optional<Day> findTodayByUser(User user);

}
