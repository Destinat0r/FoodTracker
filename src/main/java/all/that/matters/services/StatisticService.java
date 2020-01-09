package all.that.matters.services;

import all.that.matters.dao.StatisticRepository;
import all.that.matters.domain.Statistic;
import all.that.matters.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticService {

    private StatisticRepository statisticRepository;

    @Autowired
    public StatisticService(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    public List<Statistic> findForToday() {
        LocalDate today = LocalDate.now();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return statisticRepository.findAllConsumedFromTodayByUserId(user.getId());
    }

    public void create(Statistic statistic) {
        statisticRepository.save(statistic);
    }
}
