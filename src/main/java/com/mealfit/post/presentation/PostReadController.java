package com.mealfit.post.presentation;


import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.post.application.PostReadService;
import com.mealfit.post.presentation.dto.response.PostResponse;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
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
    public ResponseEntity<List<PostResponse>> readAll(@AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestParam(defaultValue = "" + Long.MAX_VALUE) Long lastId,
          @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = DEFAULT_PAGE_SIZE) Pageable pageable) {

        if(userDetails.getUser() == null){
            return ResponseEntity.status(HttpStatus.OK).body(postReadService.getReadAll(pageable,lastId,null));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(postReadService.getReadAll(pageable,lastId,userDetails.getUser().getId()));}

    }


    //상세페이지 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> readOne(@PathVariable Long postId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if(userDetails.getUser() == null){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(postReadService.getReadOne(postId,null));
        } else{
            return ResponseEntity.status(HttpStatus.OK)
              .body(postReadService.getReadOne(postId,userDetails.getUser().getId()));
        }
    }


    @PostMapping("/{postId}/likeIt")
    public boolean addlike(@PathVariable Long postId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails){

        boolean result = postReadService.saveLike(postId,userDetails.getUser());

        return result;
    }

//    @GetMapping("/{postId}/likeIt")
//    public boolean findLike(@PathVariable Long postId,
//                            @AuthenticationPrincipal UserDetailsImpl userDetails ){
//        boolean result = postReadService.findLike(postId,userDetails.getUser());
//        return result;
//    }

//    @GetMapping("/main")
//    public ResponseEntity<List<PostResponse>> getBest(@PathVariable Long postId){
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(postReadService.getList(postId));
//    }

}

