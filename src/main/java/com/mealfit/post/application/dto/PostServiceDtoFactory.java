package com.mealfit.post.application.dto;

import com.mealfit.post.application.dto.request.PostCreateRequestDto;
import com.mealfit.post.application.dto.request.PostDeleteReqeustDto;
import com.mealfit.post.application.dto.request.PostDetailRequestDto;
import com.mealfit.post.application.dto.request.PostLikeRequestDto;
import com.mealfit.post.application.dto.request.PostListRequestDto;
import com.mealfit.post.application.dto.request.PostUpdateRequestDto;
import com.mealfit.post.presentation.dto.request.PostRequest;
import com.mealfit.user.domain.User;
import org.springframework.data.domain.Pageable;

public class PostServiceDtoFactory {

    public static PostCreateRequestDto postCreateRequestDto(PostRequest request, User user) {
        return PostCreateRequestDto.builder()
              .content(request.getContent())
              .postImageList(request.getPostImageList())
              .userId(user.getId())
              .build();
    }

    public static PostUpdateRequestDto postUpdateRequestDto(Long postId, PostRequest request, User user) {
        return PostUpdateRequestDto.builder()
              .postId(postId)
              .content(request.getContent())
              .postImageList(request.getPostImageList())
              .userId(user.getId())
              .build();
    }

    public static PostDeleteReqeustDto postDeleteReqeustDto(Long postId, User user) {
        return new PostDeleteReqeustDto(postId, user.getId());
    }

    public static PostLikeRequestDto postLikeRequestDto(Long postId, User user) {
        return new PostLikeRequestDto(postId, user.getId());
    }

    public static PostDetailRequestDto postDetailRequestDto(Long postId, User user) {
        if (user == null) {
            return new PostDetailRequestDto(postId, null);
        }
        return new PostDetailRequestDto(postId, user.getId());
    }

    public static PostListRequestDto postListRequestDto(Pageable pageable, Long lastId, User user) {
        if (user == null) {
            return new PostListRequestDto(pageable, lastId, null);
        }
        return new PostListRequestDto(pageable, lastId, user.getId());
    }
}
