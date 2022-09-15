package com.mealfit.comment.presentation.dto.request;


import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest implements Serializable {

    @NotBlank(message = "수정할 내용을 입력해주세요")
    private String content;
}
