package org.training.food.tracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.training.food.tracker.model.Day;
import org.training.food.tracker.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayRepo extends JpaRepository<Day, Long> {

    Optional<Day> findByUserAndDate(User user, LocalDate date);
    List<Day> findAllByUserOrderByDateDesc(User user);
    void update(Day day);
}
