package umc.healthper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.healthper.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    /**
     * 회원 등록
     */
    public void save(Member member) {
        em.persist(member);
    }

    /**
     * 회원 조회
     */
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }

    public List<Member> findByNickName(String nickName) {
        return em.createQuery("select m from Member m where m.nickName = :name")
                .setParameter("name", nickName)
                .getResultList();
    }

    public List<Member> findByKakaoIdx(Long kakaoIdx) {
        return em.createQuery("select m from Member m where m.kakaoIdx = :kakaoIdx")
                .setParameter("kakaoIdx", kakaoIdx)
                .getResultList();
    }
}
