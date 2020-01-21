package org.training.food_tracker.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EventDTOsPack {
    private LocalDate date;
    private List<EventDTO> eventsOfTheDay;
    private BigDecimal totalCalories;
    private boolean isNormExceeded;
    private BigDecimal exceededCalories;
}