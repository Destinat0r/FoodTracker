package all.that.matters.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EventDto {
    private String foodName;
    private Double foodAmount;
    private Double totalCalories;
    private LocalDateTime timestamp;
}
