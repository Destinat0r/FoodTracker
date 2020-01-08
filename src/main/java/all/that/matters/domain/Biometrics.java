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

    @Column(name = "age", nullable = false)
    private Double age;

    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "height", nullable = false)
    private Double height;

    @Column(name = "lifestyle", nullable = false)
    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

    @Column(name = "norm", nullable = false)
    private Double dailyNorm;

    @Column(name = "consumed_today")
    private Double consumedToday;

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

    public void addToConsumed(Food food) {
        consumedToday += food.getCalories();
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

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setConsumedToday(Double consumedToday) {
        this.consumedToday = consumedToday;
    }
}
