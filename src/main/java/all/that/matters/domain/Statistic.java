package all.that.matters.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ActionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    private Food food;
    private Double amount;
    private Action action;
    private LocalDateTime dateTime;
}
