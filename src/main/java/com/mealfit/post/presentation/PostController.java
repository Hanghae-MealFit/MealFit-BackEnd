package com.mealfit.post.presentation;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.post.application.PostService;
import com.mealfit.post.application.dto.PostServiceDtoFactory;
import com.mealfit.post.application.dto.request.PostCreateRequestDto;
import com.mealfit.post.application.dto.request.PostDeleteReqeustDto;
import com.mealfit.post.application.dto.request.PostUpdateRequestDto;
import com.mealfit.post.presentation.dto.request.PostRequest;
import com.mealfit.post.presentation.dto.response.PostCUDResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //게시글작성
    @PostMapping
    public ResponseEntity<PostCUDResponse> createPost(@Valid PostRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        PostCreateRequestDto requestDto = PostServiceDtoFactory.postCreateRequestDto(
              request, userDetailsImpl.getUser());

        PostCUDResponse responseDto = postService.createPost(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body(responseDto);
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostDeleteReqeustDto requestDto = PostServiceDtoFactory.postDeleteReqeustDto(postId,
              userDetails.getUser());

        postService.deletePost(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("삭제완료!");
    }


    //게시글 수정
    @PostMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @Valid PostRequest postDto,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        PostUpdateRequestDto requestDto = PostServiceDtoFactory
              .postUpdateRequestDto(postId, postDto, userDetailsImpl.getUser());

        postService.updatePost(requestDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료!");
    }


}
