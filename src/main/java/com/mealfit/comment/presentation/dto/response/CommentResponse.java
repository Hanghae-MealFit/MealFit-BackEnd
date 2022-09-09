package com.mealfit.comment.presentation.dto.response;


import com.mealfit.comment.domain.Comment;

import java.io.Serializable;
import lombok.*;


@ToString
@NoArgsConstructor
@Getter
public class CommentResponse implements Serializable {

    private Long commentId;
    private Long postId;
    private String content;
    private UserInfoDto userDto;
    private int like;

    public CommentResponse(Comment comment, String nickname, String profileImage) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPostId();
        this.userDto = new UserInfoDto(nickname, profileImage);
        this.like = comment.getLikeIt();
    }

    @Data
    @AllArgsConstructor
    public static class UserInfoDto {

        private String nickname;
        private String profileImage;
    }
}
