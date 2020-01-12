package all.that.matters.dto;

import all.that.matters.domain.Event;
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

public class EventDtoOld {

    private User user;
    private Map<LocalDate, List<Event>> dateToEvents;

    public EventDtoOld(List<Event> events, User user) {
        this.user = user;
        this.dateToEvents = mapEventsToDay(events);
    }



    private Map<LocalDate, List<Event>> mapEventsToDay(List<Event> events) {
        List<LocalDate> days = events.stream()
                                       .map(event -> event.getTimestamp().toLocalDate())
                                       .collect(Collectors.toList());

        Map<LocalDate, List<Event>> dateToEvents = new TreeMap<>();

        days.forEach(day -> {
            dateToEvents.put(day, new ArrayList<Event>());
            sortEventsByDay(events, dateToEvents, day);
        });

        return dateToEvents;
    }

    private void sortEventsByDay(List<Event> events, Map<LocalDate, List<Event>> dateToEvents,
            LocalDate day) {
        for (Event event : events) {
            if (event.getTimestamp().toLocalDate().equals(day)) {
                List<Event> list = dateToEvents.get(day);
                list.add(event);
            }
        }
    }
}
