package umc.healthper.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.post.Post;
import umc.healthper.domain.report.PostReport;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    /**
     * 동일한 신고이력이 존재하는지 여부 확인
     *
     * @param member 신고 주체 (Member)
     * @param reportedPost 신고하려는 Post
     * @return 존재 여부 (true, false)
     */
    boolean existsByMemberAndReportedPost(Member member, Post reportedPost);
}
