package com.mealfit.post.application.dto.request;

import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class PostListRequestDto {

    private Pageable pageable;
    private Long lastId;
    private Long userId;

    public PostListRequestDto(Pageable pageable, Long lastId, Long userId) {
        this.pageable = pageable;
        this.lastId = lastId;
        this.userId = userId;
    }
}
