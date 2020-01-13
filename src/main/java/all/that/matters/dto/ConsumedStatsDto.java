package all.that.matters.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ConsumedStatsDto {
    private BigDecimal caloriesConsumed;
    private BigDecimal exceededCalories;
    private boolean isDailyNormExceeded;
}
