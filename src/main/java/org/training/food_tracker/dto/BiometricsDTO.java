package org.training.food_tracker.dto;

import lombok.*;
import org.training.food_tracker.model.Lifestyle;
import org.training.food_tracker.model.Sex;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class BiometricsDTO {

    @NotNull(message = "{userDTO.constraints.age.not_blank}")
    @Min(value = 18, message = "{userDTO.constraints.age.min}")
    @Max(value = 200, message = "{userDTO.constraints.age.max}")
    private BigDecimal age;

    @NotNull(message = "{userDTO.constraints.weight.not_blank}")
    @Min(value = 1, message = "{userDTO.constraints.weight.min}")
    @Max(value = 500, message = "{userDTO.constraints.weight.max}")
    private BigDecimal weight;

    @NotNull(message = "{userDTO.constraints.height.not_blank}")
    @Min(value = 1, message = "{userDTO.constraints.height.min}")
    @Max(value = 300, message = "{userDTO.constraints.height.max}")
    private BigDecimal height;

    private Sex sex;
    private Lifestyle lifestyle;
    private BigDecimal dailyNorm;
}
