package com.mealfit.bodyInfo.application;

import com.mealfit.bodyInfo.application.dto.request.BodyInfoChangeRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoRequestDto;
import com.mealfit.bodyInfo.application.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.bodyInfo.application.dto.response.BodyInfoResponseDto;
import com.mealfit.bodyInfo.domain.BodyInfo;
import com.mealfit.bodyInfo.domain.BodyInfoRepository;
import com.mealfit.bodyInfo.presentation.dto.request.BodyInfoDeleteRequestDto;
import com.mealfit.exception.bodyInfo.BodyInfoNotFoundException;
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
    public Long saveBodyInfo(BodyInfoSaveRequestDto dto) {
        BodyInfo bodyInfo = BodyInfo.createBodyInfo(dto.getUserId(), dto.getWeight(),
              dto.getSavedDate());
        BodyInfo savedBodyInfo = bodyInfoRepository.save(bodyInfo);

        return savedBodyInfo.getId();
    }

    @Transactional
    public void changeBodyInfo(BodyInfoChangeRequestDto dto) {
        BodyInfo bodyInfo = bodyInfoRepository.findByIdAndUserId(dto.getId(), dto.getUserId())
              .orElseThrow(() -> new BodyInfoNotFoundException("존재하지 않는 체중기록입니다."));

        bodyInfo.changeWeight(dto.getWeight());
    }

    public List<BodyInfoResponseDto> showBodyInfos(BodyInfoRequestDto dto) {
        return bodyInfoRepository.findByUserIdOrderBySavedDateDesc(dto.getUserId())
              .stream()
              .map(BodyInfoResponseDto::new)
              .collect(Collectors.toList());
    }

    public BodyInfoResponseDto showBodyInfo(BodyInfoRequestDto dto) {
        BodyInfo bodyInfo = bodyInfoRepository.findByIdAndUserId(dto.getBodyInfoId(), dto.getUserId())
              .orElseThrow(() -> new BodyInfoNotFoundException("존재하지 않는 체중기록입니다."));

        return new BodyInfoResponseDto(bodyInfo);
    }

    public void deleteBodyInfo(BodyInfoDeleteRequestDto dto) {
        BodyInfo bodyInfo = bodyInfoRepository.findByIdAndUserId(dto.getBodyInfoId(), dto.getUserId())
              .orElseThrow(() -> new BodyInfoNotFoundException("존재하지 않는 체중기록입니다."));

        bodyInfoRepository.delete(bodyInfo);
    }
}
