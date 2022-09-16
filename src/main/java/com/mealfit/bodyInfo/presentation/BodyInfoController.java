package com.mealfit.bodyInfo.presentation;

import com.mealfit.bodyInfo.application.BodyInfoService;
import com.mealfit.bodyInfo.application.dto.BodyInfoServiceDtoFactory;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoChangeRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.bodyInfo.application.dto.response.BodyInfoResponseDto;
import com.mealfit.bodyInfo.presentation.dto.request.BodyInfoChangeRequest;
import com.mealfit.bodyInfo.presentation.dto.request.BodyInfoDeleteRequestDto;
import com.mealfit.bodyInfo.presentation.dto.request.BodyInfoSaveRequest;
import com.mealfit.common.wrapper.DataWrapper;
import com.mealfit.config.security.details.UserDetailsImpl;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/bodyInfo")
@RestController
public class BodyInfoController {

    private final BodyInfoService bodyInfoService;

    public BodyInfoController(BodyInfoService bodyInfoService) {
        this.bodyInfoService = bodyInfoService;
    }

    @GetMapping
    public ResponseEntity<DataWrapper<List<BodyInfoResponseDto>>> showBodyInfoList(
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BodyInfoRequestDto requestDto =
              BodyInfoServiceDtoFactory.bodyInfoRequestDto(userDetails.getUser().getId());

        List<BodyInfoResponseDto> result = bodyInfoService.showBodyInfos(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body(new DataWrapper<>(result));
    }

    @GetMapping("/{bodyInfoId}")
    public ResponseEntity<BodyInfoResponseDto> showBodyInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @PathVariable Long bodyInfoId) {

        BodyInfoRequestDto requestDto = BodyInfoServiceDtoFactory
              .bodyInfoRequestDto(userDetails.getUser().getId(), bodyInfoId);

        BodyInfoResponseDto result = bodyInfoService.showBodyInfo(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body(result);
    }

    @PostMapping
    public ResponseEntity<String> saveBodyInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestBody BodyInfoSaveRequest request) {

        BodyInfoSaveRequestDto requestDto = BodyInfoServiceDtoFactory
              .bodyInfoSaveRequestDto(request, userDetails.getUser().getId());

        bodyInfoService.saveBodyInfo(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("입력 완료!");
    }


    @PutMapping
    public ResponseEntity<String> changeBodyInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestBody BodyInfoChangeRequest request) {
        BodyInfoChangeRequestDto requestDto = BodyInfoServiceDtoFactory
              .bodyInfoChangeRequestDto(request, userDetails.getUser().getId());

        bodyInfoService.changeBodyInfo(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료!");
    }

    @DeleteMapping("/{bodyInfoId}")
    public ResponseEntity<String> removeBodyInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @PathVariable Long bodyInfoId) {
        BodyInfoDeleteRequestDto requestDto = BodyInfoServiceDtoFactory
              .bodyInfoDeleteRequestDto(bodyInfoId, userDetails.getUser().getId());

        bodyInfoService.deleteBodyInfo(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("삭제완료!");
    }
}
