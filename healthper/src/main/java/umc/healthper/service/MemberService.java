package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.exception.member.MemberDuplicateException;
import umc.healthper.exception.member.MemberNotFoundByIdException;
import umc.healthper.exception.member.MemberNotFoundByKakaoKeyException;
import umc.healthper.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원 목록 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 조회
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundByIdException::new);
    }

    public Member findByKakaoKey(Long kakaoKey) {
        return memberRepository.findByKakaoKey(kakaoKey).orElseThrow(MemberNotFoundByKakaoKeyException::new);
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByKakaoKey(member.getKakaoKey());
        if (findMember.isPresent()) {
            throw new MemberDuplicateException();
        }
    }
}
