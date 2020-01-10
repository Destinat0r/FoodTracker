package all.that.matters.services;

import all.that.matters.dao.DayRepository;
import all.that.matters.domain.Day;
import all.that.matters.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DayService {

    private DayRepository dayRepository;

    @Autowired
    public DayService(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }

    public List<Day> findAllByUser(User user) {
        return dayRepository.findAllByUser(user);
    }

    public Optional<Day> findDayByUserAndDate(User user, LocalDate date) {
        return dayRepository.findDayByUserAndDate(user, date);
    }
}
