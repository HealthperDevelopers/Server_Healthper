package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.Comment;
import umc.healthper.domain.CommentStatus;
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
        return commentRepository.findById(commentId).get();
    }

    /**
     * Comment 수정
     */
    @Transactional
    public void updateComment(Long commentId, String content) {
        validateComment(commentId);
        Comment findComment = commentRepository.findById(commentId).get();
        findComment.update(content);
    }

    /**
     * Comment 삭제
     */
    @Transactional
    public void removeComment(Long commentId) {
        validateComment(commentId);
        commentRepository.removeComment(commentId);
    }

    // 존재하지 않는 댓글인지, 이미 삭제된 댓글인지. 댓글 유효성 검증
    private void validateComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).get();
        if (findComment == null) {
            throw new IllegalStateException("존재하지 않는 댓글입니다.");
        } else if (findComment.getStatus() == CommentStatus.REMOVED) {
            throw new IllegalStateException("이미 삭제된 댓글입니다.");
        }
    }
}
