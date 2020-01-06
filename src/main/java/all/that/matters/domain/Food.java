package all.that.matters.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

@Entity
@Table(name = "food", uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Name should not be empty")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Calories should not be empty")
    @Column(name = "calories")
    private Double calories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;
}
