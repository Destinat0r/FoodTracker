package all.that.matters.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "biometrics", uniqueConstraints={@UniqueConstraint(columnNames={"owner"})})
public class Biometrics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Age can't be blank")
    @Column(name = "age")
    private Double age;

    @NotBlank(message = "Weight can't be blank")
    @Column(name = "weight")
    private Double weight;

    @NotBlank(message = "Height can't be blank")
    @Column(name = "height")
    private Double height;

    @NotBlank(message = "Lifestyle can't be blank")
    @Column(name = "lifestyle")
    private Lifestyle lifestyle;

    @NotBlank(message = "Owner can't be blank")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
