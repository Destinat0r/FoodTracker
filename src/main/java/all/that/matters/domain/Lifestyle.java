package all.that.matters.domain;

public enum Lifestyle {
    SEDENTARY(1.53),
    MODERATE(1.76),
    VIGOROUS(2.25);

    /**
     * used in total energy expenditure calculation
     */
    private Double coefficient;

    Lifestyle(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Double getCoefficient() {
        return coefficient;
    }
}
