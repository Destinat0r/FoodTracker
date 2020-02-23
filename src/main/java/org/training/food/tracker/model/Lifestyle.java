package org.training.food.tracker.model;

import java.math.BigDecimal;

/**
 * Level of physical activity of a person.
 */
public enum Lifestyle {
    SEDENTARY(new BigDecimal(1.53)),
    MODERATE(new BigDecimal(1.76)),
    VIGOROUS(new BigDecimal(2.25));

    /**
     * used in total energy expenditure calculation
     */
    private BigDecimal coefficient;

    Lifestyle(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }
}
