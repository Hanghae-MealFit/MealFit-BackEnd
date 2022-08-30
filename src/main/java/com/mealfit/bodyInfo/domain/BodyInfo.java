package com.mealfit.bodyInfo.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "body_info",
      indexes = @Index(columnList = "user_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BodyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double weight;

    private double bodyFat;

    @Column(name = "user_id")
    private Long userId;

    private LocalDate savedDate;

    public void changeWeight(double weight) {
        this.weight = weight;
    }

    private BodyInfo(Long userId, double weight, double bodyFat, LocalDate savedDate) {
        this.userId = userId;
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.savedDate = savedDate;
    }

    public static BodyInfo createBodyInfo(Long userId, double weight, double bodyFat, LocalDate savedDate) {
        return new BodyInfo(userId, weight, bodyFat, savedDate);
    }
}
