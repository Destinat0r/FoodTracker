package org.training.food.tracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.training.food.tracker.model.ConsumedFood;
import org.training.food.tracker.model.Day;

import java.util.List;

@Repository
public interface ConsumedFoodRepo extends JpaRepository<ConsumedFood, Long> {
    List<ConsumedFood> findAllByDay(Day day);
    void delete(ConsumedFood consumedFood);
}
