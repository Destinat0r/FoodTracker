package org.training.food.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "days", uniqueConstraints = {@UniqueConstraint(columnNames={"date", "user_id"})})
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "day",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ConsumedFood> consumedFoods;

    @Column(name = "calories_consumed")
    private BigDecimal caloriesConsumed;

    @Column(name = "is_daily_norm_exceeded")
    private boolean isDailyNormExceeded;

    @Column(name = "exceeded_calories")
    private BigDecimal exceededCalories;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Override public String toString() {
        return "Day{" + "id=" + id + ", consumedFoods=" + consumedFoods + '}';
    }
}
