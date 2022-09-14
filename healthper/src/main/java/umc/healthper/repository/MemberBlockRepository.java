package umc.healthper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberBlock;

import java.util.Optional;

public interface MemberBlockRepository extends JpaRepository<MemberBlock, Long> {

    /**
     * Member(차단 주체), BlockedMember(차단 대상)을 전달받아 해당하는 MemberBlock 객체를 반환한다.
     *
     * @param member 차단 주체
     * @param blockedMember 차단 대상
     * @return 조회한 MemberBlock 객체
     */
    Optional<MemberBlock> findByMemberAndBlockedMember(Member member, Member blockedMember);
}
