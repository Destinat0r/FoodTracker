package all.that.matters.services;

import all.that.matters.repo.ExceededNormEventRepo;
import all.that.matters.model.ExceededNormEvent;
import all.that.matters.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
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

        createIfNotExistUpdateOtherwise(exceededNormEvent, user);
    }

    public void createIfNotExistUpdateOtherwise(ExceededNormEvent exceededNormEvent, User user) {
        if (!exceededNormEventRepo.existsByUserAndDate(user, exceededNormEvent.getDate())) {
            create(exceededNormEvent);
        } else {
            exceededNormEventRepo.updateExcessiveCaloriesByDateAndUserId(
                    exceededNormEvent.getExcessive_calories(),
                    exceededNormEvent.getDate(),
                    user.getId()
            );
        }
    }
}
