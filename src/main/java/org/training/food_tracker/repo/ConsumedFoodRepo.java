package org.training.food_tracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.training.food_tracker.model.ConsumedFood;

@Repository
public interface ConsumedFoodRepo extends JpaRepository<ConsumedFood, Long> {

}
