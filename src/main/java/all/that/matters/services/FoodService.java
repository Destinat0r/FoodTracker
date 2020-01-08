package all.that.matters.services;

import all.that.matters.dao.FoodRepository;
import all.that.matters.domain.Food;
import all.that.matters.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    public void add(Food food) {
        foodRepository.save(food);
    }

    public List<Food> findAllByOwner(User user) {
        return foodRepository.findByOwner(user.getId());
    }

    public Optional<Food> findById(Long id) {
        return foodRepository.findById(id);
    }

    public List<Food> findAllCommon() {
        return foodRepository.findAllCommon();
    }
}
