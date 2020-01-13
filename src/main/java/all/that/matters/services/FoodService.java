package all.that.matters.services;

import all.that.matters.dao.FoodRepository;
import all.that.matters.domain.Food;
import all.that.matters.domain.User;
import all.that.matters.dto.FoodDto;
import all.that.matters.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service public class FoodService {

    private FoodRepository foodRepository;

    @Autowired public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    public void add(FoodDto foodDto) {
        Food food = Food.builder()
                        .name(foodDto.getName())
                        .calories(foodDto.getCalories())
                        .owner(ContextUtils.getPrincipal())
                        .build();

        foodRepository.save(food);
    }

    public List<FoodDto> findAllByOwner(User user) {
        List<FoodDto> foodDtos = new ArrayList<>();
        foodRepository.findByOwner(user.getId()).forEach(
                food -> foodDtos.add(
                            FoodDto.builder()
                                    .name(food.getName())
                                    .calories(food.getCalories())
                                    .build())
        );
        return foodDtos;
    }

    public Optional<Food> findById(Long id) {
        return foodRepository.findById(id);
    }

    public List<FoodDto> findAllCommonFoodInDtos() {
        List<FoodDto> commonFoodDtos = new ArrayList<>();
        foodRepository.findAllCommon().forEach(food -> commonFoodDtos.add(
                FoodDto.builder()
                        .name(food.getName())
                        .calories(food.getCalories())
                        .build()
        ));

        return commonFoodDtos;
    }

    public void remove(Food food) {
        foodRepository.delete(food);
    }
}
