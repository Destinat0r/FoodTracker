package all.that.matters.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EventsDto {
    private List<EventDto> eventsOfTheDay;
    private BigDecimal totalCalories;
    private boolean isNormExceeded;
    private BigDecimal exceededCalories;
}