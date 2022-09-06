package com.mealfit.bodyInfo.application;

import com.mealfit.bodyInfo.domain.BodyInfo;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoChangeRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.bodyInfo.application.dto.response.BodyInfoResponseDto;
import com.mealfit.bodyInfo.domain.BodyInfoRepository;
import com.mealfit.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BodyInfoService {

    private final BodyInfoRepository bodyInfoRepository;

    public BodyInfoService(BodyInfoRepository bodyInfoRepository) {
        this.bodyInfoRepository = bodyInfoRepository;
    }

    @Transactional
    public void saveBodyInfo(BodyInfoSaveRequestDto dto) {
        BodyInfo bodyInfo = BodyInfo.createBodyInfo(dto.getUserId(), dto.getWeight(),
              dto.getSavedDate());
        bodyInfoRepository.save(bodyInfo);
    }

    @Transactional
    public void changeBodyInfo(BodyInfoChangeRequestDto dto) {
        BodyInfo bodyInfo = bodyInfoRepository.findByIdAndUserId(dto.getId(), dto.getUserId())
              .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체중기록입니다."));

        bodyInfo.changeWeight(dto.getWeight());
    }

    public List<BodyInfoResponseDto> showBodyInfos(User user) {
        return bodyInfoRepository.findByUserIdOrderBySavedDateDesc(user.getId())
              .stream()
              .map(BodyInfoResponseDto::new)
              .collect(Collectors.toList());
    }

    public BodyInfoResponseDto showBodyInfo(User user, Long bodyInfoId) {
        BodyInfo bodyInfo = bodyInfoRepository.findByIdAndUserId(bodyInfoId, user.getId())
              .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 체중기록입니다."));

        return new BodyInfoResponseDto(bodyInfo);
    }
}
