package all.that.matters.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @JoinColumn(name = "food_id", nullable = false)
    @ManyToOne
    private Food food;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "total_calories")
    private Double totalCalories;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
