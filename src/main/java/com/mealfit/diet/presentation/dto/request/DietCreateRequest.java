package com.mealfit.diet.presentation.dto.request;

import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietCreateRequest implements Serializable {
    @NotNull
    private Long foodId;  // 음식 ID

    @NotNull
    private Long foodWeight; // 음식 중량

    @NotNull
    private String status; // 아침 점심 저녁

    @NotNull
    private LocalDate date; // 기록 일자

    public Diet toEntity() {
        return new Diet(foodId, DietStatus.valueOf(status), foodWeight, date);
    }
}
