package all.that.matters.services;

import all.that.matters.dao.FoodRepository;
import all.that.matters.domain.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    private FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Food> findAll() {
        return foodRepository.findAll();
    }
}
