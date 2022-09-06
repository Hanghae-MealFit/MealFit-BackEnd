package com.mealfit.diet.application;

import com.mealfit.diet.application.dto.request.DietChangeRequestDto;
import com.mealfit.diet.application.dto.request.DietCreateRequestDto;
import com.mealfit.diet.application.dto.request.DietListByDateRequestDto;
import com.mealfit.diet.application.dto.response.DietResponseByDateDto;
import com.mealfit.diet.application.dto.response.DietResponseDto;
import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietRepository;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.food.domain.Food;
import com.mealfit.food.domain.FoodRepository;
import com.mealfit.user.application.dto.response.UserNutritionGoalResponseDto;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DietService {

    private final DietRepository dietRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    //식단 조회
    @Transactional(readOnly = true)
    public DietResponseByDateDto getDietById(DietListByDateRequestDto dto) {

        // 얘도 여러개니까 리스트로
        List<Diet> dietList = dietRepository.findByDietDateAndUserId(dto.getDate(),
              dto.getUserId());

        List<DietResponseDto> dietResponseDtoList = dietList.stream()
              .map(diet -> {
                  Food food = foodRepository.findById(diet.getFoodId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 음식입니다."));

                  return new DietResponseDto(diet, food);
              })
              .collect(Collectors.toList());
        
        User user = userRepository.findById(dto.getUserId())
              .orElseThrow(() -> new UserNotFoundException("존재하지 않는 회원입니다."));

        UserNutritionGoalResponseDto userGoalDto =
              new UserNutritionGoalResponseDto(user);

        return new DietResponseByDateDto(dietResponseDtoList, userGoalDto);
    }


    // 식단 입력
    public Long createDiet(DietCreateRequestDto dto) {

        //post 저장
        Diet dietEntity = dto.toEntity();
        dietEntity.settingUserInfo(dto.getUserId());

        Diet savedDiet = dietRepository.save(dietEntity);

        //return 값 생성
        return savedDiet.getId();
    }

    // 식단 수정
    public void updateDiet(DietChangeRequestDto dto) {
        //item
        Diet diet = getDietById(dto.getDietId());

        validateUser(dto.getUserId(), diet.getUserId());

        diet.update(dto.getFoodId(), dto.getFoodWeight());
    }

    private static void validateUser(Long userId, Long dietUserId) {
        if (!userId.equals(dietUserId)) {
            throw new IllegalArgumentException("작성자가 아니므로, 해당 식단을 수정할 수 없습니다.");
        }
    }

    // 식단 삭제
    public void deleteDiet(Long dietId, User user) {
        //유효성 검사
        Diet diet = getDietById(dietId);

        //작성자 검사
        validateUser(user.getId(), diet.getUserId());

        dietRepository.delete(diet);
    }

    private Diet getDietById(Long dietId) {
        return dietRepository.findById(dietId)
              .orElseThrow(() -> new IllegalArgumentException("기록한 식단이 없습니다."));
    }
}
