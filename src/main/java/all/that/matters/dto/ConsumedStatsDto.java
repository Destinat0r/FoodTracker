package all.that.matters.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ConsumedStatsDto {
    private Double caloriesConsumed;
    private Double exceededCalories;
    private boolean isDailyNormExceeded;
}
