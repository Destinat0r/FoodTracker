package org.training.food.tracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "consumed_foods")
public class ConsumedFood {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="day_id", nullable=false)
    private Day day;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "total_calories")
    private BigDecimal totalCalories;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Override public String toString() {
        return "ConsumedFood{" + "food='" + name + '\'' + ", amount=" + amount + ", totalCalories=" + totalCalories
                       + '}';
    }
}
