package all.that.matters.dto;

import all.that.matters.domain.Statistic;
import all.that.matters.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor

public class StatisticsDto {

    private User user;
    private Map<LocalDate, List<Statistic>> dateToStatisticsMap;

    public StatisticsDto(List<Statistic> statsOfUser, User user) {
        this.user = user;
        this.dateToStatisticsMap = mapStatisticsToDay(statsOfUser);
    }

    private Map<LocalDate, List<Statistic>> mapStatisticsToDay(List<Statistic> statsOfUser) {
        List<LocalDate> days = statsOfUser.stream()
                                       .map(statistic -> statistic.getTimestamp().toLocalDate())
                                       .collect(Collectors.toList());

        Map<LocalDate, List<Statistic>> dateToStaticsMap = new TreeMap<>();

        days.forEach(day -> {
            dateToStaticsMap.put(day, new ArrayList<Statistic>());
            sortStatisticsByDay(statsOfUser, dateToStaticsMap, day);
        });

        return dateToStaticsMap;
    }

    private void sortStatisticsByDay(List<Statistic> statsOfUser, Map<LocalDate, List<Statistic>> dateToStatics,
            LocalDate day) {
        for (Statistic stat : statsOfUser) {
            if (stat.getTimestamp().toLocalDate().equals(day)) {
                List<Statistic> list = dateToStatics.get(day);
                list.add(stat);
            }
        }
    }
}
