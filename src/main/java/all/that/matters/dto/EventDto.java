package all.that.matters.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EventDto {
    private String foodName;
    private BigDecimal foodAmount;
    private BigDecimal totalCalories;
    private LocalDateTime timestamp;
}
