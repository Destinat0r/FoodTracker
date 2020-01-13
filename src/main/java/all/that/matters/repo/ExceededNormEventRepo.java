package all.that.matters.repo;

import all.that.matters.model.ExceededNormEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface ExceededNormEventRepo extends JpaRepository<ExceededNormEvent, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE exceed_events SET excessive_calories = ?1 WHERE date = ?2 AND user_id = ?3", nativeQuery = true)
    void updateExcessiveCaloriesByDateAndUserId(Double calories, LocalDate date, Long userId);

}
