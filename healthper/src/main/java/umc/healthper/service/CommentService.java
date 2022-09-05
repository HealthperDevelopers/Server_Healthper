package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.comment.CommentStatus;
import umc.healthper.exception.comment.CommentAlreadyRemovedException;
import umc.healthper.exception.comment.CommentNotFoundException;
import umc.healthper.exception.comment.CommentUnauthorizedException;
import umc.healthper.repository.comment.CommentRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * Comment 등록(저장)
     *
     * @param comment
     * @return 생성된 Comment 객체 return
     */
    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * Comment 조회
     *
     * @param commentId
     * @return 조회된 Comment 객체 return
     */
    public Comment findById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        validateRemovedComment(comment);
        return comment;
    }

    /**
     * Comment 수정
     *
     * @param commentId
     * @param content
     */
    @Transactional
    public void updateComment(Long loginMemberId, Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        validateRemovedComment(comment);
        validateCommentAuthority(loginMemberId, comment);

        comment.update(content);
    }

    /**
     * Comment 삭제
     *
     * @param commentId
     */
    @Transactional
    public void removeComment(Long loginMemberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        validateRemovedComment(comment);
        validateCommentAuthority(loginMemberId, comment);

        commentRepository.removeComment(comment);
    }

    /**
     * 이미 삭제된 댓글인지 검증. 이미 삭제되었다면 CommentAlreadyRemovedException throw
     *정
     * @param comment
     */
    private void validateRemovedComment(Comment comment) {
        if (comment.getStatus() == CommentStatus.REMOVED) {
            throw new CommentAlreadyRemovedException();
        }
    }

    /**
     * 게시글에 대한 수정/삭제 권한이 있는지 검증. 작성자에게만 write 권한이 부여된다.
     * 게시글의 수정/삭제 권한이 없는 사용자라면 PostUnauthorizedException throw
     *
     * @param memberId
     * @param comment
     */
    private void validateCommentAuthority(Long memberId, Comment comment) {
        if (!memberId.equals(comment.getMember().getId())) {
            throw new CommentUnauthorizedException();
        }
    }
}
