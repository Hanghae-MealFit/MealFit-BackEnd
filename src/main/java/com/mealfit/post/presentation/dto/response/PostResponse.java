package com.mealfit.post.presentation.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.mealfit.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
public class PostResponse implements Serializable {

    private Long postId;

    private String nickname;

    private String profileImage;

    private String content;

    private int like;

    private int view;

    private List<String> images;

    private Boolean liked;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    @Builder
    public PostResponse(Long postId, String nickname, String profileImage, String content, int like,
          int view, List<String> images, Boolean liked, LocalDateTime createdAt,
          LocalDateTime updatedAt) {
        this.postId = postId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.content = content;
        this.like = like;
        this.view = view;
        this.images = images;
        this.liked = liked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
