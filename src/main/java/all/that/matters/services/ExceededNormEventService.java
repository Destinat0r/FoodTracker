package all.that.matters.services;

import all.that.matters.repo.ExceededNormEventRepo;
import all.that.matters.model.ExceededNormEvent;
import all.that.matters.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ExceededNormEventService {

    private ExceededNormEventRepo exceededNormEventRepo;

    @Autowired
    public ExceededNormEventService(ExceededNormEventRepo exceededNormEventRepo) {
        this.exceededNormEventRepo = exceededNormEventRepo;
    }

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
        tryToCreateOrUpdateIfExist(exceededNormEvent, user);
    }

    private void tryToCreateOrUpdateIfExist(ExceededNormEvent exceededNormEvent, User user) {
        try {
            create(exceededNormEvent);
        } catch (Exception ex) {
            exceededNormEventRepo.updateExcessiveCaloriesByDateAndUserId(
                    exceededNormEvent.getExcessive_calories(),
                    exceededNormEvent.getDate(),
                    user.getId()
            );
        }
    }
}
