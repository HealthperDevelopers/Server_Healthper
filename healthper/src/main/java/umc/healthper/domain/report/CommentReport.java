package umc.healthper.domain.report;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.member.Member;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "COMMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_comment_id")
    private Comment reportedComment;

    //== Constructor ==//
    public CommentReport(Member member, Comment reportedComment) {
        super(member);
        this.reportedComment = reportedComment;
    }

    //== Create Method ==//
    public static CommentReport createCommentReport(Member member, Comment comment) {
        return new CommentReport(member, comment);
    }
}
