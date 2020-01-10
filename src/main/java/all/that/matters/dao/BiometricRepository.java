package all.that.matters.dao;

import all.that.matters.domain.Biometrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometricRepository extends JpaRepository<Biometrics, Long> {

}
