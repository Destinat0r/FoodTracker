package all.that.matters.dao;

import all.that.matters.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiometricRepository extends JpaRepository<Statistic, Long> {
    
}
