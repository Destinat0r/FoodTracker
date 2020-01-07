package all.that.matters.services;

import all.that.matters.dao.StatisticRepository;
import all.that.matters.domain.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
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
        return statisticRepository.findAllByDateTime_DayOfYear(today.getDayOfYear());
    }
}
