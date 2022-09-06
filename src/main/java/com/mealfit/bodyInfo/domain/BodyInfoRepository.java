package com.mealfit.bodyInfo.domain;

import com.mealfit.bodyInfo.domain.BodyInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyInfoRepository extends JpaRepository<BodyInfo, Long> {

    List<BodyInfo> findByUserIdOrderBySavedDateDesc(Long userId);

    List<BodyInfo> findTop10ByUserIdOrderBySavedDateDesc(Long userId);

    Optional<BodyInfo> findByIdAndUserId(Long id, Long userId);

}
