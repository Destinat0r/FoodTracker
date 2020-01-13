package all.that.matters.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "age", nullable = false)
    private BigDecimal age;

    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "height", nullable = false)
    private BigDecimal height;

    @Column(name = "lifestyle", nullable = false)
    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

    @Column(name = "norm", nullable = false)
    private BigDecimal dailyNorm;

    /**
     * Total energy expenditure calculation using Harris–Benedict equation
     * @return daily norm of calories
     */
    public BigDecimal calculateDailyNorm() {
        if (this.sex == Sex.MALE) {
            return (new BigDecimal(66.5)
                            .add(new BigDecimal(13.75).multiply(weight))
                            .add(new BigDecimal(5.003).multiply(height))
                            .subtract(new BigDecimal(6.755).multiply(age)))
                           .multiply(lifestyle.getCoefficient());
        } else {
            return (new BigDecimal(655.1)
                            .add(new BigDecimal(9.563).multiply(weight))
                            .add(new BigDecimal(1.850).multiply(height))
                            .subtract(new BigDecimal(4.676).multiply(age)))
                           .multiply(lifestyle.getCoefficient());
        }
    }

    public void setDailyNorm() {
        this.dailyNorm = calculateDailyNorm();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public void setLifestyle(Lifestyle lifestyle) {
        this.lifestyle = lifestyle;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
