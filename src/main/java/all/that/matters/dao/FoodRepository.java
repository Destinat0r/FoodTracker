package all.that.matters.dao;

import all.that.matters.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query(value = "SELECT id, name, calories, user_id FROM food WHERE user_id = ?1", nativeQuery = true)
    List<Food> findByOwner(Long userId);
}
