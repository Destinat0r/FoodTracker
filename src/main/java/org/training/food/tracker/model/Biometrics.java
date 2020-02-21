package org.training.food.tracker.model;

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
