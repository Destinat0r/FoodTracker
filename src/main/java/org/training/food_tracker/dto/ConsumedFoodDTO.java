package org.training.food_tracker.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class ConsumedFoodDTO {

    private String name;
    private BigDecimal amount;
    private BigDecimal totalCalories;
    private LocalTime time;
}
