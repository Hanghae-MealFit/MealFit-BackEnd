package com.mealfit.diet.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Generated;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@DynamicUpdate
@Entity
public class Diet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DietStatus status;

    @Column(nullable = false)
    private LocalDate dietDate;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long foodId;

    @Column(nullable = false)
    private double foodWeight;

    public Diet(Long foodId, DietStatus status, Long foodWeight, LocalDate dietDate) {
        this.foodId = foodId;
        this.status = status;
        this.foodWeight = foodWeight;
        this.dietDate = dietDate;
    }

    protected Diet() {

    }

    public void settingUserInfo(Long userId) {
        this.userId = userId;
    }

    public void update(Long foodId, double foodWeight) {
        this.foodId = foodId;
        this.foodWeight = foodWeight;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Diet diet = (Diet) o;
        return Objects.equals(id, diet.id);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
