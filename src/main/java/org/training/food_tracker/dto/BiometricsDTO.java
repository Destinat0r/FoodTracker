package org.training.food_tracker.dto;

import java.math.BigDecimal;

public class BiometricsDTO {

    private BigDecimal age;
    private String sex;
    private BigDecimal weight;
    private BigDecimal height;
    private String lifestyle;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BiometricsDTO biometricsDTO;

        private Builder() {
            this.biometricsDTO = new BiometricsDTO();
        }

        public Builder age(BigDecimal age) {
            biometricsDTO.setAge(age);
            return this;
        }

        public Builder sex(String sex) {
            biometricsDTO.setSex(sex);
            return this;
        }

        public Builder weight(BigDecimal weight) {
            biometricsDTO.setWeight(weight);
            return this;
        }

        public Builder height(BigDecimal height) {
            biometricsDTO.setHeight(height);
            return this;
        }

        public Builder lifestyle(String lifestyle) {
            biometricsDTO.setLifestyle(lifestyle);
            return this;
        }

        public BiometricsDTO build() {
            return biometricsDTO;
        }
    }

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public String getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(String lifestyle) {
        this.lifestyle = lifestyle;
    }
}
