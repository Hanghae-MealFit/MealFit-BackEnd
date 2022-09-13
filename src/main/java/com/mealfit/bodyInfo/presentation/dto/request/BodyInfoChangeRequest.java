package com.mealfit.bodyInfo.presentation.dto.request;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodyInfoChangeRequest implements Serializable {

    @NotNull(message = "ID 값 누락")
    private Long id;

    @Max(value = 0)
    @NotNull(message = "몸무게를 입력해주세요.")
    private double weight;

    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @NotNull(message = "날짜를 입력해 주세요")
    private LocalDate savedDate;
}
