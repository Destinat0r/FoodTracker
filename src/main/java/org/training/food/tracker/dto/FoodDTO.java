package org.training.food.tracker.dto;

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
//    @Pattern(regexp = "^(?:\\b\\S{1,5}\\b\\s*)+$", message = "Word should not be longer that 20")
    private String name;
    @NotNull
    private BigDecimal totalCalories;
    private BigDecimal amount;
}
