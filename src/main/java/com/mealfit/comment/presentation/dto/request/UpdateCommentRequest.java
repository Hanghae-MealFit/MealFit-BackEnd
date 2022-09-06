package com.mealfit.comment.presentation.dto.request;


import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UpdateCommentRequest implements Serializable {

    @NotBlank(message = "수정할 내용을 입력해주세요")
    private String content;
}
