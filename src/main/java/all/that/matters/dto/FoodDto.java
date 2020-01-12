package all.that.matters.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class FoodDto {
    private String name;
    private Double calories;
    private Double amount;
}
