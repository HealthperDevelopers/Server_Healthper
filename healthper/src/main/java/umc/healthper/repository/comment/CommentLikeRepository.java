package umc.healthper.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.comment.CommentLike;
import umc.healthper.domain.member.Member;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    /**
     * Member와 Comment에 해당하는 CommentLike 객체 조회
     *
     * @param member  좋아요한 Member
     * @param comment 좋아요한 Comment
     * @return 조회된 CommentLike 객체
     */
    Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);

    /**
     * Member와 Comment에 해당하는 CommentLike 존재 여부 조회
     *
     * @param member  좋아요한 Member
     * @param comment 좋아요한 Comment
     * @return CommentLike 존재 여부
     */
    boolean existsByMemberAndComment(Member member, Comment comment);
}
