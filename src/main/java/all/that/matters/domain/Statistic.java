package all.that.matters.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
public class Statistic {
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

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private Action action;

    @Column(name = "data_time", nullable = false)
    private LocalDateTime dateTime;
}
