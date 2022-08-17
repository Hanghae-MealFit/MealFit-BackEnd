package com.mealfit.post.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostsResponseDto {
    private Long postId;
    private String image;
    private int like;
    private int view;


}