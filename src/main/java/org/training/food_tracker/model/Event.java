package org.training.food_tracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column(name = "food", nullable = false)
    private String food;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "total_calories")
    private BigDecimal totalCalories;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
