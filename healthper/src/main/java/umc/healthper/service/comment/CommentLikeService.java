package umc.healthper.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.comment.CommentLike;
import umc.healthper.domain.member.Member;
import umc.healthper.exception.comment.CommentNotFoundException;
import umc.healthper.exception.commentlike.CommentLikeAlreadyExistException;
import umc.healthper.exception.commentlike.CommentLikeNotFoundException;
import umc.healthper.exception.member.MemberNotFoundException;
import umc.healthper.repository.MemberRepository;
import umc.healthper.repository.comment.CommentLikeRepository;
import umc.healthper.repository.comment.CommentRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentLikeService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    /**
     * 댓글 좋아요 추가
     *
     * @param memberId  좋아요한 Member의 id
     * @param commentId 좋아요한 Comment의 id
     */
    @Transactional
    public void addLike(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        validateAlreadyLike(member, comment);

        CommentLike commentLike = CommentLike.createCommentLike(member, comment);
        commentLikeRepository.save(commentLike);
    }

    /**
     * 댓글 좋아요 취소
     *
     * @param memberId  좋아요한 Member의 id
     * @param commentId 좋아요한 Comment의 id
     */
    @Transactional
    public void cancelLike(Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        CommentLike commentLike = commentLikeRepository.findByMemberAndComment(member, comment)
                .orElseThrow(CommentLikeNotFoundException::new);
        member.getCommentLikes().remove(commentLike);
        comment.getLikes().remove(commentLike);
        commentLikeRepository.delete(commentLike);
    }

    private void validateAlreadyLike(Member member, Comment comment) {
        if (commentLikeRepository.existsByMemberAndComment(member, comment)) {
            throw new CommentLikeAlreadyExistException();
        }
    }
}
