package org.training.food_tracker.services;

import org.training.food_tracker.repo.ExceededEventRepo;
import org.training.food_tracker.model.ExceededEvent;
import org.training.food_tracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
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

    public List<ExceededEvent> findAllByUser(User user) {
        return exceededEventRepo.findAllByUser(user);
    }
}
