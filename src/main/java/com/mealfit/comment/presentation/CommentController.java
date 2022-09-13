package com.mealfit.comment.presentation;

import com.mealfit.comment.application.CommentService;
import com.mealfit.comment.application.dto.CommentServiceDtoFactory;
import com.mealfit.comment.application.dto.request.CommentLikeRequestDto;
import com.mealfit.comment.application.dto.request.CommentSaveRequestDto;
import com.mealfit.comment.application.dto.request.CommentDeleteRequestDto;
import com.mealfit.comment.application.dto.request.CommentUpdateRequestDto;
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
     * JSON -> 필드 하나인 경우 - Front -> 내용 으로 전송을 하면....
     * <p>
     * Back - content = "content : 내용"으로 받아버림
     * <p>
     * 해결책 : Front측 에게 {content : 내용} 이 아닌 {내용} 만 보내게 해라 FrameWork -> json 형식으로 보내는게 아니네?라 착각함
     * (application-www-encodedType) Form-Data 이럴땐 Content-Type : Application/JSON
     */
    @PostMapping("/{postId}/comment")
    public ResponseEntity<String> createComment(@PathVariable Long postId,
          @Valid @RequestBody CreateCommentRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentSaveRequestDto requestDto = CommentServiceDtoFactory.commentCreateRequestDto(
              postId,
              userDetails.getUser(),
              request);
        commentService.createComment(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("작성 완료!");

    }
    //delete
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentDeleteRequestDto requestDto = CommentServiceDtoFactory.commentDeleteRequestDto(
              commentId, userDetails.getUser());
        commentService.deleteComment(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
              .body("삭제완료");
    }
    //update
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
          @Valid @RequestBody UpdateCommentRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentUpdateRequestDto requestDto = CommentServiceDtoFactory.commentUpdateRequestDto(
              commentId, userDetails.getUser(), request);

        commentService.updateComment(requestDto);
        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료!");
    }
    //comment List
    @GetMapping("/{postId}/comment")
    public ResponseEntity<CommentWrapper<List<CommentResponse>>> listComment(@PathVariable Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null){
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new CommentWrapper<>(commentService.getCommentList(postId,null)));
        }else{
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new CommentWrapper<>(commentService.getCommentList(postId,userDetails.getUser().getId())));
        }

    }
    //like
    @PostMapping("/comment/{commentId}/likeIt")
    public ResponseEntity<Boolean> addlike(@PathVariable Long commentId,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentLikeRequestDto requestDto = CommentServiceDtoFactory.commentLikeRequestDto(
              commentId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
              .body(commentService.saveLike(requestDto));
    }

    @Data
    @AllArgsConstructor
    static class CommentWrapper<T> {

        private T comments;
    }

}
