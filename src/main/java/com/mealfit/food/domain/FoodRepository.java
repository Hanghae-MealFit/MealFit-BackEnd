package com.mealfit.food.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByIdLessThanAndFoodNameContaining(long lastId, String foodName, Pageable pageable);
}
