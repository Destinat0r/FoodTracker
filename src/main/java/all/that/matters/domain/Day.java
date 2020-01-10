package all.that.matters.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Day {
    private LocalDate date;
    private User user;
    private List<Food> consumed;

    @Column(name = "consumed_today")
    private Double consumedToday = 0.0;

    private boolean isNormExceeded;
}

