package com.mealfit.post.application.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long postId;

    private String nickname;

    private String profileImage;

    private String content;

    private int like;

    private int view;

    private String image;

    private boolean liked;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PostResponseDto(Long postId, String nickname, String profileImage, String content, int like,
          int view, String images, boolean liked, LocalDateTime createdAt,
          LocalDateTime updatedAt) {
        this.postId = postId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.content = content;
        this.like = like;
        this.view = view;
        this.image = image;
        this.liked = liked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
