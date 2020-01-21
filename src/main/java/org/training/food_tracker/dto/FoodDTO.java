package org.training.food_tracker.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class FoodDTO {
    private String name;
    private BigDecimal calories;
    private BigDecimal amount;
}
