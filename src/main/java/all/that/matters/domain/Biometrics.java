package all.that.matters.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "biometrics")
public class Biometrics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @NotBlank(message = "Age can't be blank")
    @Column(name = "age", nullable = false)
    private Double age;

    @NotBlank(message = "Weight can't be blank")
    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @NotBlank(message = "Weight can't be blank")
    @Column(name = "weight", nullable = false)
    private Double weight;

    @NotBlank(message = "Height can't be blank")
    @Column(name = "height", nullable = false)
    private Double height;

    @NotBlank(message = "Lifestyle can't be blank")
    @Column(name = "lifestyle", nullable = false)
    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

    @Column(name = "norm", nullable = false)
    private Double dailyNorm;

    /**
     * Total energy expenditure calculation using Harris–Benedict equation
     * @return daily norm of calories
     */
    public Double calculateDailyNorm() {
        if (this.sex == Sex.MALE) {
            return (66.5 + 13.75*weight + 5.003*height - 6.755*age)*lifestyle.getCoefficient();
        } else {
            return (655.1 + 9.563*weight + 1.850*height - 4.676*age)*lifestyle.getCoefficient();
        }
    }

    public void setDailyNorm() {
        this.dailyNorm = calculateDailyNorm();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setLifestyle(Lifestyle lifestyle) {
        this.lifestyle = lifestyle;
    }
}
