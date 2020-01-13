package all.that.matters.services;

import all.that.matters.dao.ExceededNormEventRepo;
import all.that.matters.domain.ExceededNormEvent;
import all.that.matters.domain.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ExceededNormEventService {

    private ExceededNormEventRepo exceededNormEventRepo;

    public void create(ExceededNormEvent event) {
        exceededNormEventRepo.save(event);
    }

    public void createExceededNormEvent(User user, Double caloriesConsumedToday) {
        Double userNorm = user.getBiometrics().getDailyNorm();
        ExceededNormEvent exceededNormEvent = ExceededNormEvent.builder()
                                                      .user(user)
                                                      .excessive_calories(caloriesConsumedToday - userNorm)
                                                      .date(LocalDate.now())
                                                      .build();
        exceededNormEventRepo.save(exceededNormEvent);

    }

}
