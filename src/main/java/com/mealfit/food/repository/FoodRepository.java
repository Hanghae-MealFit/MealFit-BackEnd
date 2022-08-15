package com.mealfit.food.repository;

import com.mealfit.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByFoodName(String foodname);

}
