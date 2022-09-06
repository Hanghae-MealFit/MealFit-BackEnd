package com.mealfit.post.presentation;


import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.post.application.PostReadService;
import com.mealfit.post.presentation.dto.response.PostResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostReadController {

    private final PostReadService postReadService;

    public static final int DEFAULT_PAGE_SIZE = 12;
// 전체 리스트 조회

    @GetMapping
    // sort = "id", direction = Sort.Direction.DESC 아이디로 내림차순 정렬
    public ResponseEntity<List<PostResponse>> readAll(
          @RequestParam(defaultValue = "" + Long.MAX_VALUE) Long lastId,
          @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = DEFAULT_PAGE_SIZE) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
              .body(postReadService.getReadAll(pageable, lastId));
    }


    //상세페이지 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> readOne(@PathVariable Long postId) {

        return ResponseEntity.status(HttpStatus.OK)
              .body(postReadService.getReadOne(postId));
    }


    @PostMapping("/{postId}/likeIt")
    public boolean addlike(@PathVariable Long postId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails){

        boolean result = postReadService.saveLike(postId,userDetails.getUser());

        return result;
    }


}

