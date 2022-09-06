package com.mealfit.post.presentation.dto.request;



import com.mealfit.post.domain.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {

    @NotBlank
    private String content;

    @NotNull
    private List<MultipartFile> postImageList;

    public Post toEntity() {
        return new Post(content);
    }

}
