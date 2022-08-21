package com.mealfit.BodyInfo.controller;

import com.mealfit.BodyInfo.dto.request.BodyInfoChangeRequestDto;
import com.mealfit.BodyInfo.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.BodyInfo.dto.response.BodyInfoResponseDto;
import com.mealfit.BodyInfo.service.BodyInfoService;
import com.mealfit.common.wrapper.DataWrapper;
import com.mealfit.config.security.details.UserDetailsImpl;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class BodyInfoController {

    private final BodyInfoService bodyInfoService;

    public BodyInfoController(BodyInfoService bodyInfoService) {
        this.bodyInfoService = bodyInfoService;
    }

    @GetMapping("/bodyInfo")
    public ResponseEntity<DataWrapper<List<BodyInfoResponseDto>>> showAllUserBodyInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BodyInfoResponseDto> result = bodyInfoService.showBodyInfos(
              userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
              .body(new DataWrapper<>(result));
    }

    @GetMapping("/bodyInfo/{bodyInfoId}")
    public ResponseEntity<BodyInfoResponseDto> showUserBodyInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @PathVariable Long bodyInfoId) {
        BodyInfoResponseDto result = bodyInfoService.showBodyInfo(
              userDetails.getUser(), bodyInfoId);

        return ResponseEntity.status(HttpStatus.OK)
              .body(result);
    }

    @PostMapping("/bodyInfo")
    public ResponseEntity<String> saveUserBodyInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestBody BodyInfoSaveRequestDto dto) {
        bodyInfoService.saveBodyInfo(userDetails.getUser(), dto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("입력 완료!");
    }


    @PutMapping("/bodyInfo")
    public ResponseEntity<String> changeUserBodyInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestBody BodyInfoChangeRequestDto dto) {
        bodyInfoService.changeBodyInfo(userDetails.getUser(), dto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료!");
    }
}
