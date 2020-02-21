package org.training.food_tracker.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

public class ConsumedFoodDTO {

    private String name;
    private BigDecimal amount;
    private BigDecimal totalCalories;
    private LocalTime time;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ConsumedFoodDTO consumedFoodDTO;

        private Builder() {
            this.consumedFoodDTO = new ConsumedFoodDTO();
        }

        public Builder name(String name) {
            consumedFoodDTO.setName(name);
            return this;
        }

        public Builder amount(BigDecimal amount) {
            consumedFoodDTO.setAmount(amount);
            return this;
        }

        public Builder totalCalories(BigDecimal totalCalories) {
            consumedFoodDTO.setTotalCalories(totalCalories);
            return this;
        }

        public Builder time(LocalTime localTime) {
            consumedFoodDTO.setTime(localTime);
            return this;
        }

        public ConsumedFoodDTO build() {
            return consumedFoodDTO;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(BigDecimal totalCalories) {
        this.totalCalories = totalCalories;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
