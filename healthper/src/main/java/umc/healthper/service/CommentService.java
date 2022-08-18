package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.Comment;
import umc.healthper.domain.CommentStatus;
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
     */
    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * Comment 조회
     */
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    /**
     * Comment 수정
     */
    @Transactional
    public void updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        validateComment(comment);
        comment.update(content);
    }

    /**
     * Comment 삭제
     */
    @Transactional
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        validateComment(comment);
        commentRepository.removeComment(comment);
    }

    // 이미 삭제된 댓글인지. 댓글 유효성 검증
    private void validateComment(Comment comment) {
        if (comment.getStatus() == CommentStatus.REMOVED) {
            throw new CommentAlreadyRemovedException();
        }
    }
}
