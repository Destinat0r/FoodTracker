package all.that.matters.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "exceed_events", uniqueConstraints = {@UniqueConstraint(columnNames={"date", "user_id"})})
public class ExceededEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column(name = "excessive_calories", nullable = false)
    private BigDecimal excessive_calories;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}
