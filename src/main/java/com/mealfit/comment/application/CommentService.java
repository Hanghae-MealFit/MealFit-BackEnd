package com.mealfit.comment.application;

import com.mealfit.comment.application.dto.request.CommentDeleteRequestDto;
import com.mealfit.comment.application.dto.request.CommentLikeRequestDto;
import com.mealfit.comment.application.dto.request.CommentSaveRequestDto;
import com.mealfit.comment.application.dto.request.CommentUpdateRequestDto;
import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.domain.CommentLike;
import com.mealfit.comment.domain.CommentLikeRepository;
import com.mealfit.comment.domain.CommentRepository;
import com.mealfit.comment.presentation.dto.CommentControllerDtoFactory;
import com.mealfit.comment.presentation.dto.response.CommentResponse;
import com.mealfit.exception.authentication.UnAuthorizedUserException;
import com.mealfit.exception.comment.CommentNotFoundException;
import com.mealfit.exception.post.PostNotFoundException;
import com.mealfit.exception.user.UserNotFoundException;
import com.mealfit.post.domain.PostRepository;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import java.util.List;
import java.util.Optional;
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
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;

    //작성하기
    public CommentResponse createComment(CommentSaveRequestDto dto) {

        // 굳이 Post 인스턴스가 필요하지 않기 때문에 있는지 없는지만 체크해주는 exists 메서드 사용.
        if (!postRepository.existsById(dto.getPostId())) {
            throw new PostNotFoundException("게시글이 없습니다.");
        }

        User user = findUserById(dto.getUserId());

        Comment comment = dto.toEntity();
        comment.settingUserInfo(user);
        Comment savedComment = commentRepository.save(comment);

        return CommentControllerDtoFactory.commentResponse(savedComment, false);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
              .orElseThrow(() -> new UserNotFoundException("없는 회원입니다."));
    }

    //삭제하기
    public void deleteComment(CommentDeleteRequestDto dto) {
        Comment comment = findByCommentId(dto.getCommentId());

        validateUser(dto.getUserId(), comment.getUser().getId());

        commentRepository.deleteById(dto.getCommentId());
    }

    //수정하기
    public void updateComment(CommentUpdateRequestDto dto) {
        Comment comment = findByCommentId(dto.getCommentId());

        validateUser(dto.getUserId(), comment.getUser().getId());

        comment.update(dto.getContent());
    }

    private void validateUser(Long userId, Long writerId) {
        if (!userId.equals(writerId)) {
            throw new UnAuthorizedUserException("작성자가 아니므로 수정할 수 없습니다.");
        }
    }

    // 자주 사용하는 코드 리팩토링.
    private Comment findByCommentId(Long dto) {
        return commentRepository.findById(dto)
              .orElseThrow(() -> new CommentNotFoundException("수정하려는 댓글이 없습니다."));
    }

    //댓글 리스트
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentList(Long postId, Long userId) {

        return commentRepository.findByPostIdOrderByCreatedAt(postId)
              .stream()
              .map(comment -> {
                  User user = findUserById(comment.getUser().getId());
                  return new CommentResponse(comment,
                        user.getUserProfile().getNickname(),
                        user.getUserProfile().getProfileImage(),
                        commentLikeRepository.existsByCommentIdAndUserId(comment.getId(), userId));
              })
              .collect(Collectors.toList());
    }

    public boolean saveLike(CommentLikeRequestDto dto) {
        Optional<CommentLike> findLike = commentLikeRepository.findByCommentIdAndUserId(
              dto.getCommentId(), dto.getUserId());
        if (findLike.isEmpty()) {
            CommentLike commentLike = CommentLike.builder()
                  .commentId(dto.getCommentId())
                  .userId(dto.getUserId())
                  .build();
            commentLikeRepository.save(commentLike);

            Comment comment = commentRepository.findById(dto.getCommentId())
                  .orElseThrow(() -> new CommentNotFoundException("해당 Comment가 없습니다."));

            comment.likeComment();
            return true;
        } else {
            commentLikeRepository.deleteByCommentIdAndUserId(dto.getCommentId(), dto.getUserId());

            Comment comment = commentRepository.findById(dto.getCommentId())
                  .orElseThrow(() -> new CommentNotFoundException("해당 Comment가 없습니다."));

            comment.unlikeComment();
            return false;
        }
    }
}
