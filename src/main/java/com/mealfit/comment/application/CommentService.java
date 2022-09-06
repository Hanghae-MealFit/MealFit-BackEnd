package com.mealfit.comment.application;

import com.mealfit.comment.application.dto.request.CreateCommentRequestDto;
import com.mealfit.comment.application.dto.request.UpdateCommentRequestDto;
import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.domain.CommentRepository;
import com.mealfit.comment.presentation.dto.response.CommentResponse;
import com.mealfit.post.domain.PostRepository;
import com.mealfit.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //작성하기
    public Long createComment(CreateCommentRequestDto dto) {

        // 굳이 Post 인스턴스가 필요하지 않기 때문에 있는지 없는지만 체크해주는 exists 메서드 사용.
        if (!postRepository.existsById(dto.getPostId())) {
            throw new IllegalArgumentException("게시글이 없습니다.");
        }

        Comment comment = dto.toEntity();

        commentRepository.save(comment);

        return comment.getId();
    }

    //삭제하기
    public void deleteComment(Long commentId, User user) {
        Comment comment = findByCommentId(commentId);

        validateUser(user.getId(), comment.getUserId());

        commentRepository.deleteById(commentId);
    }

    //수정하기
    public void updateComment(UpdateCommentRequestDto dto) {
        Comment comment = findByCommentId(dto.getCommentId());

        validateUser(dto.getUserId(), comment.getUserId());

        comment.update(dto.getContent());
    }

    private void validateUser(Long userId, Long writerId) {
        if (!userId.equals(writerId)) {
            throw new IllegalArgumentException("작성자가 아니므로 수정할 수 없습니다.");
        }
    }

    // 자주 사용하는 코드 리팩토링.
    private Comment findByCommentId(Long dto) {
        return commentRepository.findById(dto)
              .orElseThrow(() -> new IllegalArgumentException("수정하려는 댓글이 없습니다."));
    }

    //댓글 리스트
    @Transactional(readOnly = true)
    public List<CommentResponse> listComment(Long postId) {

        return commentRepository.findByPostIdOrderByCreatedAt(postId).stream()
        .map(CommentResponse::new)
        .collect(Collectors.toList());
    }
}
