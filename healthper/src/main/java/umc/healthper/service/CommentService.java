package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.comment.CommentStatus;
import umc.healthper.exception.comment.CommentAlreadyRemovedException;
import umc.healthper.exception.comment.CommentNotFoundException;
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
    public void updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        validateRemovedComment(comment);
        comment.update(content);
    }

    /**
     * Comment 삭제
     *
     * @param commentId
     */
    @Transactional
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        validateRemovedComment(comment);
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
}
