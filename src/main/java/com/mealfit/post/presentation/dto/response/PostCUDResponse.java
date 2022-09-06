package com.mealfit.post.presentation.dto.response;

import com.mealfit.post.domain.Post;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostCUDResponse {

    private Long postId;
    private String content;
    private Long userId;
    private List<String> imageUrls;

    public PostCUDResponse(Post post) {
        this.postId = post.getId();
        this.userId = post.getUserId();
        this.content = post.getContent();
        this.imageUrls = post.getImageUrls();
    }
}

