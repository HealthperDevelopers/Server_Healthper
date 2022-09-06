package umc.healthper.domain.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import umc.healthper.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    //== 생성 메서드 ==//
    public static CommentLike createCommentLike(Member member, Comment comment) {
        CommentLike commentLike = new CommentLike();
        commentLike.setMember(member);
        commentLike.setComment(comment);
        return commentLike;
    }

    //== 연관관계 편의 메서드 ==//
    private void setMember(Member member) {
        this.member = member;
        member.getCommentLikes().add(this);
    }

    private void setComment(Comment comment) {
        this.comment = comment;
        comment.getLikes().add(this);
    }
}
