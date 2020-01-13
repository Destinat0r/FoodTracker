package all.that.matters.services;

import all.that.matters.dao.ExceededNormEventRepo;
import all.that.matters.domain.ExceededNormEvent;
import org.springframework.stereotype.Service;

@Service
public class ExceededNormEventService {

    private ExceededNormEventRepo exceededNormEventRepo;

    public void create(ExceededNormEvent event) {
        exceededNormEventRepo.save(event);
    }
}
