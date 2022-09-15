package com.mealfit.unit.diet.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.common.factory.UserFactory;
import com.mealfit.diet.application.DietService;
import com.mealfit.diet.application.dto.request.DietCreateRequestDto;
import com.mealfit.diet.application.dto.request.DietDeleteRequestDto;
import com.mealfit.diet.application.dto.request.DietListByDateRequestDto;
import com.mealfit.diet.application.dto.request.DietUpdateRequestDto;
import com.mealfit.diet.application.dto.response.DietResponseByDateDto;
import com.mealfit.diet.domain.Diet;
import com.mealfit.diet.domain.DietRepository;
import com.mealfit.diet.domain.DietStatus;
import com.mealfit.exception.diet.DietNotFoundException;
import com.mealfit.exception.food.FoodNotFoundException;
import com.mealfit.exception.user.InvalidUserException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.food.domain.Food;
import com.mealfit.food.domain.FoodRepository;
import com.mealfit.user.domain.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DietServiceTest {

    @InjectMocks
    private DietService dietService;

    @Mock
    private DietRepository dietRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FoodRepository foodRepository;

    @DisplayName("createDiet()는")
    @Nested
    class Testing_createDiet {

        @DisplayName("완전한 DietCreateRequestDto를 받으면 Diet를 생성한다.")
        @Test
        void createDiet_success() {

            // given
            Diet diet = new Diet(1L, 1L, DietStatus.BREAKFAST, 150, LocalDate.of(2022, 9, 1));
            DietCreateRequestDto requestDto = new DietCreateRequestDto(1L, 1L, 150,
                  DietStatus.BREAKFAST, LocalDate.of(2022, 9, 1));

            given(dietRepository.save(any(Diet.class))).willReturn(diet);

            // when
            Long dietId = dietService.createDiet(requestDto);

            // then
            Assertions.assertThat(dietId).isEqualTo(diet.getId());
            verify(dietRepository, times(1)).save(any(Diet.class));
        }
    }

    @DisplayName("updateDiet()는")
    @Nested
    class Testing_updateDiet {

        @DisplayName("완전한 DietUpdateRequestDto를 받으면 Diet를 생성한다.")
        @Test
        void updateDiet_success() {

            // given
            Diet diet = new Diet(1L, 1L, DietStatus.BREAKFAST, 150, LocalDate.of(2022, 9, 1));
            diet.settingUserInfo(1L);

            DietUpdateRequestDto requestDto = new DietUpdateRequestDto(1L, 1L, 2L, 150);

            given(dietRepository.findById(anyLong())).willReturn(Optional.of(diet));

            // when then
            dietService.updateDiet(requestDto);
        }

        @DisplayName("식단을 쓴 회원이 아닌 경우 수정이 불가능하다.")
        @Test
        void deleteDiet_fail() {

            // given
            Diet diet = new Diet(1L, 1L, DietStatus.BREAKFAST, 150, LocalDate.of(2022, 9, 1));
            diet.settingUserInfo(2L);

            DietUpdateRequestDto requestDto = new DietUpdateRequestDto(1L, 1L, 2L, 150);

            given(dietRepository.findById(anyLong())).willReturn(Optional.of(diet));

            // when then
            Assertions.assertThatThrownBy(() -> dietService.updateDiet(requestDto))
                  .isInstanceOf(InvalidUserException.class);
        }
    }

    @DisplayName("deleteDiet()는")
    @Nested
    class Testing_deleteDiet {

        @DisplayName("완전한 DietUpdateRequestDto를 받으면 Diet를 생성한다.")
        @Test
        void deleteDiet_success() {

            // given
            Diet diet = new Diet(1L, 1L, DietStatus.BREAKFAST, 150, LocalDate.of(2022, 9, 1));
            diet.settingUserInfo(1L);

            given(dietRepository.findById(anyLong())).willReturn(Optional.of(diet));

            DietDeleteRequestDto requestDto = new DietDeleteRequestDto(1L, 1L);

            // when then
            dietService.deleteDiet(requestDto);
        }

        @DisplayName("식단을 쓴 회원이 아닌 경우 삭제가 불가능하다.")
        @Test
        void deleteDiet_fail() {

            // given
            Diet diet = new Diet(1L, 1L, DietStatus.BREAKFAST, 150, LocalDate.of(2022, 9, 1));
            diet.settingUserInfo(2L);

            given(dietRepository.findById(anyLong())).willReturn(Optional.of(diet));

            DietDeleteRequestDto requestDto = new DietDeleteRequestDto(1L, 1L);

            // when then
            Assertions.assertThatThrownBy(() -> dietService.deleteDiet(requestDto))
                  .isInstanceOf(InvalidUserException.class);
        }

        @DisplayName("없는 Diet ID면 DietNotFoundException이 발생한다.")
        @Test
        void deleteDiet_NotFound_fail() {

            // given
            given(dietRepository.findById(anyLong())).willReturn(Optional.empty());

            DietDeleteRequestDto requestDto = new DietDeleteRequestDto(1L, 1L);

            // when then
            Assertions.assertThatThrownBy(() -> dietService.deleteDiet(requestDto))
                  .isInstanceOf(DietNotFoundException.class);
        }
    }

    @DisplayName("getDietListByDate()는")
    @Nested
    class Testing_getDietListByDate {

        @DisplayName("해당 날짜의 DietResponseByDateDto 를 반환한다.")
        @Test
        void getDietListByDate_success() {

            // given
            Diet diet1 = new Diet(1L, 1L, DietStatus.BREAKFAST, 150, LocalDate.of(2022, 9, 1));
            Diet diet2 = new Diet(2L, 2L, DietStatus.LUNCH, 150, LocalDate.of(2022, 9, 1));
            Diet diet3 = new Diet(3L, 3L, DietStatus.DINNER, 150, LocalDate.of(2022, 9, 1));
            diet1.settingUserInfo(1L);
            diet2.settingUserInfo(1L);
            diet3.settingUserInfo(1L);

            DietListByDateRequestDto requestDto = new DietListByDateRequestDto(1L,
                  LocalDate.of(2022, 9, 1));

            given(dietRepository.findByDietDateAndUserId(any(LocalDate.class),
                  anyLong())).willReturn(List.of(diet1, diet2, diet3));
            given(foodRepository.findById(anyLong()))
                  .willReturn(Optional.of(new Food(1L, "사과", 150, 120, 30, 5, 2, "전국")));
            given(userRepository.findById(anyLong())).willReturn(
                  Optional.of(UserFactory.basicUser(1L, "회원1")));

            // when
            DietResponseByDateDto dietListByDate = dietService.getDietListByDate(requestDto);

            verify(dietRepository, times(1))
                  .findByDietDateAndUserId(any(LocalDate.class), anyLong());
            verify(foodRepository, times(3)).findById(anyLong());
            verify(userRepository, times(1)).findById(anyLong());
        }

        @DisplayName("없는 foodId 라면 FoodNotFoundException이 발생한다.")
        @Test
        void getDietListByDate_foodNotFound_fail() {

            // given
            Diet diet1 = new Diet(1L, 1L, DietStatus.BREAKFAST, 150, LocalDate.of(2022, 9, 1));
            Diet diet2 = new Diet(2L, 2L, DietStatus.LUNCH, 150, LocalDate.of(2022, 9, 1));
            Diet diet3 = new Diet(3L, 3L, DietStatus.DINNER, 150, LocalDate.of(2022, 9, 1));
            diet1.settingUserInfo(1L);
            diet2.settingUserInfo(1L);
            diet3.settingUserInfo(1L);

            DietListByDateRequestDto requestDto = new DietListByDateRequestDto(1L,
                  LocalDate.of(2022, 9, 1));

            given(dietRepository.findByDietDateAndUserId(any(LocalDate.class),
                  anyLong())).willReturn(List.of(diet1, diet2, diet3));
            given(foodRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            Assertions.assertThatThrownBy(() -> dietService.getDietListByDate(requestDto))
                  .isInstanceOf(FoodNotFoundException.class);

            verify(dietRepository, times(1))
                  .findByDietDateAndUserId(any(LocalDate.class), anyLong());
        }

        @DisplayName("없는 userId 라면 UserNotFoundException이 발생한다.")
        @Test
        void getDietListByDate_userNotFound_fail() {

            // given
            Diet diet1 = new Diet(1L, 1L, DietStatus.BREAKFAST, 150, LocalDate.of(2022, 9, 1));
            Diet diet2 = new Diet(2L, 2L, DietStatus.LUNCH, 150, LocalDate.of(2022, 9, 1));
            Diet diet3 = new Diet(3L, 3L, DietStatus.DINNER, 150, LocalDate.of(2022, 9, 1));
            diet1.settingUserInfo(1L);
            diet2.settingUserInfo(1L);
            diet3.settingUserInfo(1L);

            DietListByDateRequestDto requestDto = new DietListByDateRequestDto(1L,
                  LocalDate.of(2022, 9, 1));

            given(dietRepository.findByDietDateAndUserId(any(LocalDate.class),
                  anyLong())).willReturn(List.of(diet1, diet2, diet3));
            given(foodRepository.findById(anyLong()))
                  .willReturn(Optional.of(new Food(1L, "사과", 150, 120, 30, 5, 2, "전국")));
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // when then
            Assertions.assertThatThrownBy(() -> dietService.getDietListByDate(requestDto))
                  .isInstanceOf(UserNotFoundException.class);

            verify(dietRepository, times(1))
                  .findByDietDateAndUserId(any(LocalDate.class), anyLong());
            verify(foodRepository, times(3)).findById(anyLong());
        }
    }

}
