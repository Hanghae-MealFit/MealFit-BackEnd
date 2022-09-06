package com.mealfit.comment.presentation.dto.request;


import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest implements Serializable {

    @NotBlank(message = "내용을 입력해주세요")
    private String content;
}
