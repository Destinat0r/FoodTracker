package all.that.matters.dao;

import all.that.matters.domain.ExceededNormEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceededNormEventRepo extends JpaRepository<ExceededNormEvent, Long> {

}
