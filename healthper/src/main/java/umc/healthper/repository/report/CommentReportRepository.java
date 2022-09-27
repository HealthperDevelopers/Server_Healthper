package umc.healthper.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.comment.Comment;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.report.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    /**
     * 동일한 신고이력이 존재하는지 여부 확인
     *
     * @param member 신고 주체 (Member)
     * @param reportedComment 신고하려는 Comment
     * @return 존재 여부 (true, false)
     */
    boolean existsByMemberAndReportedComment(Member member, Comment reportedComment);
}
