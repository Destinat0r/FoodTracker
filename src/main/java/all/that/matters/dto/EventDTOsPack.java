package all.that.matters.dto;

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
    private List<EventDto> eventsOfTheDay;
    private BigDecimal totalCalories;
    private boolean isNormExceeded;
    private BigDecimal exceededCalories;
}