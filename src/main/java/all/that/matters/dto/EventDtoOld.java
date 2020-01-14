package all.that.matters.dto;

import all.that.matters.model.Event;
import all.that.matters.model.User;
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


    public EventDtoOld(List<Event> events, User user) {
        this.user = user;
    }




}
