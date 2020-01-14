package all.that.matters.services;

import all.that.matters.repo.ExceededEventRepo;
import all.that.matters.model.ExceededEvent;
import all.that.matters.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class ExceededEventService {

    private ExceededEventRepo exceededEventRepo;

    @Autowired
    public ExceededEventService(ExceededEventRepo exceededEventRepo) {
        this.exceededEventRepo = exceededEventRepo;
    }

    public void create(ExceededEvent event) {
        exceededEventRepo.save(event);
    }

    public void createExceededNormEvent(User user, BigDecimal caloriesConsumedToday) {
        BigDecimal userNorm = user.getBiometrics().getDailyNorm();
        ExceededEvent exceededEvent = ExceededEvent.builder()
                                                      .user(user)
                                                      .excessive_calories(caloriesConsumedToday.subtract(userNorm))
                                                      .date(LocalDate.now())
                                                      .build();

        createIfNotExistUpdateOtherwise(exceededEvent, user);
    }

    public void createIfNotExistUpdateOtherwise(ExceededEvent exceededEvent, User user) {
        if (!exceededEventRepo.existsByUserAndDate(user, exceededEvent.getDate())) {
            create(exceededEvent);
        } else {
            exceededEventRepo.updateExcessiveCaloriesByDateAndUserId(
                    exceededEvent.getExcessive_calories(),
                    exceededEvent.getDate(),
                    user.getId()
            );
        }
    }
}
