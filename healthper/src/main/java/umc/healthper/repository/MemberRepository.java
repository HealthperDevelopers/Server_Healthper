package umc.healthper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByKakaoKey(Long kakaoKey);

    boolean existsByNicknameIs(String nickname);
}
