package com.mealfit.unit.bodyInfo.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mealfit.bodyInfo.application.BodyInfoService;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoChangeRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoDeleteRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.bodyInfo.application.dto.response.BodyInfoResponseDto;
import com.mealfit.bodyInfo.domain.BodyInfo;
import com.mealfit.bodyInfo.domain.BodyInfoRepository;
import com.mealfit.exception.bodyInfo.BodyInfoNotFoundException;
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

@DisplayName("BodyInfoService - 체중기록 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class BodyInfoServiceTest {

    @InjectMocks
    private BodyInfoService bodyInfoService;

    @Mock
    private BodyInfoRepository bodyInfoRepository;

    @DisplayName("saveBodyInfo() 메서드는 ")
    @Nested
    class Testing_SaveBodyInfo {

        @DisplayName("완전한 RequestDto 를 받으면 저장한다.")
        @Test
        void saveBodyInfo_Success() {
            BodyInfoSaveRequestDto requestDto = new BodyInfoSaveRequestDto(1L, 80.0,
                  LocalDate.of(2022, 8, 30));

            given(bodyInfoRepository.save(any(BodyInfo.class)))
                  .willReturn(BodyInfo.createBodyInfo(
                        1L,
                        80.0,
                        LocalDate.of(2022, 8, 30)));

            bodyInfoService.saveBodyInfo(requestDto);

            verify(bodyInfoRepository, times(1)).save(any(BodyInfo.class));
        }
    }

    @DisplayName("changeBodyInfo() 메서드는 ")
    @Nested
    class Testing_ChangeBodyInfo {

        @DisplayName("완전한 RequestDto 를 받으면 수정한다.")
        @Test
        void changeBodyInfo_Success() {

            // given
            BodyInfoChangeRequestDto requestDto = new BodyInfoChangeRequestDto(
                  1L,
                  1L,
                  80.0,
                  LocalDate.of(2022, 8, 30));

            BodyInfo before = BodyInfo.createBodyInfo(1L,
                  80.0,
                  LocalDate.of(2022, 8, 29));

            given(bodyInfoRepository.findByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(before));

            // when
            bodyInfoService.changeBodyInfo(requestDto);

            // then
            verify(bodyInfoRepository, times(1)).findByIdAndUserId(anyLong(), anyLong());
        }

        @DisplayName("BodyInfo 가 없으면 BodyInfoNotFoundException 예외를 발생한다.")
        @Test
        void changeBodyInfo_BodyInfoNotFound_Fail() {

            // given
            BodyInfoChangeRequestDto requestDto = new BodyInfoChangeRequestDto(
                  1L,
                  1L,
                  80.0,
                  LocalDate.of(2022, 8, 30));

            given(bodyInfoRepository.findByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());

            // when
            Assertions.assertThatThrownBy(() -> bodyInfoService.changeBodyInfo(requestDto))
                  .isInstanceOf(BodyInfoNotFoundException.class);
        }
    }

    @DisplayName("showBodyInfos() 메서드는 ")
    @Nested
    class Testing_ShowBodyInfos {

        @DisplayName("해당 회원의 BodyInfo List를 보여준다.")
        @Test
        void showBodyInfos_Success() {

            // given
            BodyInfoRequestDto requestDto = new BodyInfoRequestDto(1L);

            List<BodyInfo> bodyInfoList = List.of(
                  BodyInfo.createBodyInfo(1L, 80.0, LocalDate.of(2022, 9, 1)),
                  BodyInfo.createBodyInfo(1L, 79.0, LocalDate.of(2022, 9, 2)),
                  BodyInfo.createBodyInfo(1L, 78.0, LocalDate.of(2022, 9, 3)),
                  BodyInfo.createBodyInfo(1L, 77.0, LocalDate.of(2022, 9, 4)),
                  BodyInfo.createBodyInfo(1L, 76.0, LocalDate.of(2022, 9, 5)),
                  BodyInfo.createBodyInfo(1L, 75.0, LocalDate.of(2022, 9, 6))
            );

            given(bodyInfoRepository.findByUserIdOrderBySavedDateDesc(anyLong())).willReturn(
                  bodyInfoList);

            // when
            List<BodyInfoResponseDto> responseDtoList = bodyInfoService.showBodyInfos(requestDto);

            Assertions.assertThat(responseDtoList).usingRecursiveComparison()
                  .comparingOnlyFields("id", "weight", "savedDate");

            verify(bodyInfoRepository, times(1))
                  .findByUserIdOrderBySavedDateDesc(anyLong());
        }
    }

    @DisplayName("showBodyInfo() 메서드는 ")
    @Nested
    class Testing_ShowBodyInfo {

        @DisplayName("UserId, BodyInfoId를 제공받으면 BodyInfo를 제공한다.")
        @Test
        void showBodyInfo_Success() {

            // given
            BodyInfo bodyInfo = BodyInfo.createBodyInfo(1L, 80.0, LocalDate.of(2022, 9, 1));
            BodyInfoRequestDto requestDto = new BodyInfoRequestDto(1L, 1L);

            given(bodyInfoRepository.findByIdAndUserId(anyLong(), anyLong()))
                  .willReturn(Optional.of(bodyInfo));

            // when
            BodyInfoResponseDto responseDto = bodyInfoService.showBodyInfo(requestDto);

            Assertions.assertThat(responseDto.getWeight()).isEqualTo(bodyInfo.getWeight());
            Assertions.assertThat(responseDto.getSavedDate()).isEqualTo(bodyInfo.getSavedDate());

            // then
            verify(bodyInfoRepository, times(1))
                  .findByIdAndUserId(anyLong(), anyLong());
        }

        @DisplayName("없는 값이라면 BodyInfoNOtFoundException를 발생시킨다..")
        @Test
        void showBodyInfo_Fail() {

            // given
            BodyInfoRequestDto requestDto = new BodyInfoRequestDto(1L, 1L);

            given(bodyInfoRepository.findByIdAndUserId(anyLong(), anyLong()))
                  .willReturn(Optional.empty());

            // when
            Assertions.assertThatThrownBy(() -> bodyInfoService.showBodyInfo(requestDto))
                  .isInstanceOf(BodyInfoNotFoundException.class);
        }
    }

    @DisplayName("deleteBodyInfo() 메서드는 ")
    @Nested
    class Testing_DeleteBodyInfo {

        @DisplayName("UserId, BodyInfoId를 제공받으면 BodyInfo를 제공한다.")
        @Test
        void deleteBodyInfo_Success() {

            // given
            BodyInfo bodyInfo = BodyInfo.createBodyInfo(1L, 80.0, LocalDate.of(2022, 9, 1));
            BodyInfoDeleteRequestDto requestDto = new BodyInfoDeleteRequestDto(1L, 1L);

            given(bodyInfoRepository.findByIdAndUserId(anyLong(), anyLong()))
                  .willReturn(Optional.of(bodyInfo));

            // when
            bodyInfoService.deleteBodyInfo(requestDto);

            // then
            verify(bodyInfoRepository, times(1))
                  .findByIdAndUserId(anyLong(), anyLong());
        }

        @DisplayName("없는 값이라면 BodyInfoNOtFoundException를 발생시킨다..")
        @Test
        void deleteBodyInfo_Fail() {

            // given
            BodyInfoDeleteRequestDto requestDto = new BodyInfoDeleteRequestDto(1L, 1L);

            given(bodyInfoRepository.findByIdAndUserId(anyLong(), anyLong()))
                  .willReturn(Optional.empty());

            // when
            Assertions.assertThatThrownBy(() -> bodyInfoService.deleteBodyInfo(requestDto))
                  .isInstanceOf(BodyInfoNotFoundException.class);
        }
    }
}
