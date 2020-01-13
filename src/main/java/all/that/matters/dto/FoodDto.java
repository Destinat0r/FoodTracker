package all.that.matters.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class FoodDto {
    private String name;
    private BigDecimal calories;
    private BigDecimal amount;
}
