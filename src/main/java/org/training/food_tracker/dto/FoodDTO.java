package org.training.food_tracker.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class FoodDTO {
    @NotNull
    private String name;
    @NotNull
    private BigDecimal totalCalories;
    private BigDecimal amount;
}
