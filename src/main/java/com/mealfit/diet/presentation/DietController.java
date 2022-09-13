package com.mealfit.diet.presentation;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.diet.application.DietService;
import com.mealfit.diet.application.dto.DietServiceDtoFactory;
import com.mealfit.diet.application.dto.request.DietDeleteRequestDto;
import com.mealfit.diet.application.dto.request.DietUpdateRequestDto;
import com.mealfit.diet.application.dto.request.DietCreateRequestDto;
import com.mealfit.diet.application.dto.request.DietListByDateRequestDto;
import com.mealfit.diet.application.dto.response.DietResponseByDateDto;
import com.mealfit.diet.presentation.dto.request.DietUpdateRequest;
import com.mealfit.diet.presentation.dto.request.DietSaveRequest;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    //식단 조회
    @GetMapping
    public ResponseEntity<DietResponseByDateDto> getDiet(
          @RequestParam("date")
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          LocalDate date,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        DietListByDateRequestDto requestDto = DietServiceDtoFactory.dietListByDateRequestDto(
              userDetailsImpl.getUser().getId(), date);
        return ResponseEntity.status(HttpStatus.OK)
              .body(dietService.getDietListByDate(requestDto));

    }

    //식단 입력
    @PostMapping
    public ResponseEntity<Long> saveDiet(@RequestBody DietSaveRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        DietCreateRequestDto requestDto = DietServiceDtoFactory.dietCreateRequestDto(
              userDetailsImpl.getUser().getId(), request);

        Long dietId = dietService.createDiet(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body(dietId);
    }

    //식단 수정
    @PutMapping
    public ResponseEntity<String> updateDiet(@RequestBody DietUpdateRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        DietUpdateRequestDto requestDto = DietServiceDtoFactory.dietChangeRequestDto(
              userDetails.getUser().getId(), request);

        dietService.updateDiet(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("식단 수정 완료!");
    }

    //식단 삭제
    @DeleteMapping("/{dietId}")
    public ResponseEntity<String> deleteDiet(@PathVariable Long dietId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        DietDeleteRequestDto requestDto = DietServiceDtoFactory.dietDeleteRequestDto(
              dietId, userDetails.getUser().getId());
        dietService.deleteDiet(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("식단 삭제 완료!");
    }
}
