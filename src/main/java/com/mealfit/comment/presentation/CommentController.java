package com.mealfit.comment.presentation;

import com.mealfit.comment.application.CommentService;
import com.mealfit.comment.application.dto.CommentServiceDtoFactory;
import com.mealfit.comment.application.dto.request.CreateCommentRequestDto;
import com.mealfit.comment.application.dto.request.UpdateCommentRequestDto;
import com.mealfit.comment.presentation.dto.response.CommentResponse;
import com.mealfit.comment.presentation.dto.request.CreateCommentRequest;
import com.mealfit.comment.presentation.dto.request.UpdateCommentRequest;
import com.mealfit.config.security.details.UserDetailsImpl;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     *  JSON -> 필드 하나인 경우
     *      - Front -> 내용 으로 전송을 하면....

     *        Back - content = "content : 내용"으로 받아버림
     *
     *  해결책 : Front측 에게 {content : 내용} 이 아닌 {내용} 만 보내게 해라
     *           FrameWork -> json 형식으로 보내는게 아니네?라 착각함  (application-www-encodedType) Form-Data
     *                        이럴땐 Content-Type : Application/JSON
     */
    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> createComment(@PathVariable Long postId,
                                        @Valid @RequestBody CreateCommentRequest request,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CreateCommentRequestDto requestDto = CommentServiceDtoFactory.createCommentRequestDto(
              postId,
              userDetails.getUser(),
              request);
        commentService.createComment(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("작성 완료!");

    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK)
                .body("삭제완료");
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @Valid @RequestBody UpdateCommentRequest request,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        UpdateCommentRequestDto requestDto = CommentServiceDtoFactory.updateCommentRequestDto(
              commentId, userDetails.getUser(), request);

        commentService.updateComment(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("수정 완료!");
    }

    @GetMapping("/{postId}/comment")
    public ResponseEntity<CommentWrapper<List<CommentResponse>>> listComment(@PathVariable Long postId) {
        List<CommentResponse> dtoList = commentService.listComment(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommentWrapper<>(dtoList));
    }

    @PostMapping("/comment/{commentId}/likeIt")
    public boolean addlike(@PathVariable Long commentId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails){

        boolean result = commentService.saveLike(commentId,userDetails.getUser());

        return result;
    }

    @Data
    @AllArgsConstructor
    static class CommentWrapper<T> {
        private T comments;
    }

}
