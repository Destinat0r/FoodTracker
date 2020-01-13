package all.that.matters.dao;

import all.that.matters.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepo extends JpaRepository<Food, Long> {
    @Query(value = "SELECT id, name, calories, user_id FROM food WHERE user_id = ?1", nativeQuery = true)
    List<Food> findByOwner(Long userId);

    Optional<Food> findById(Long id);

    @Query(value = "SELECT id, name, calories, user_id from food WHERE user_id IS NULL", nativeQuery = true)
    List<Food> findAllCommon();

    Optional<Food> findByName(String name);
}
