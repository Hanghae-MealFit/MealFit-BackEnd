package com.mealfit.food.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    Page<Food> findByIdLessThanAndFoodNameContaining(long lastId, String foodName, Pageable pageable);
}
