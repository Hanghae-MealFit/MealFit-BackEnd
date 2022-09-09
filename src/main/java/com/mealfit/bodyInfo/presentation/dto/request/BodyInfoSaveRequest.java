package com.mealfit.bodyInfo.presentation.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyInfoSaveRequest implements Serializable {

    @NotBlank(message = "몸무게를 입력해주세요.")
    @Min(value = 0, message = "무게는 0보다 커야 합니다.")
    private double weight;

    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @NotNull(message = "날짜를 입력해 주세요")
    private LocalDate savedDate;

}
