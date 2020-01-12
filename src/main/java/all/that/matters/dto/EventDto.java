package all.that.matters.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EventDto {
    private String foodName;
    private Double foodAmount;
    private Double totalCalories;
    private LocalDate date;
}
