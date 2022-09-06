package com.mealfit.post.application.dto.request;


import com.mealfit.post.domain.Post;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostCreateRequestDto {

    private Long userId;

    private String content;

    private List<MultipartFile> postImageList;

    @Builder
    public PostCreateRequestDto(Long userId, String content, List<MultipartFile> postImageList) {
        this.userId = userId;
        this.content = content;
        this.postImageList = postImageList;
    }

    public Post toEntity() {
        Post post = new Post(content);
        post.settingUserInfo(userId);
        return post;
    }
}
