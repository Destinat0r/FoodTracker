package all.that.matters.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "days")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    private List<Food> consumed;

    @Column(name = "consumed_today")
    private Double consumedCalories = 0.0;

    @Column(name = "norm_exceeded")
    private boolean isNormExceeded;

    @Column(name = "above_norm")
    private Double aboveNormCalories;

    public void addToConsumed(Food food) {
        consumed.add(food);
        consumedCalories += food.getCalories();
        Double norm = user.getBiometrics().getDailyNorm();

        if (norm < consumedCalories) {
            isNormExceeded = true;
            aboveNormCalories = consumedCalories - norm;
        }
    }
}

