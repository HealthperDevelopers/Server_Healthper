package umc.healthper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.healthper.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByKakaoKey(Long kakaoKey);
}
