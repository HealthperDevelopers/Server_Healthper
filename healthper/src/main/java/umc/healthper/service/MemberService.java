package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberBlock;
import umc.healthper.exception.block.MemberBlockDuplicateException;
import umc.healthper.exception.block.MemberBlockNotFoundException;
import umc.healthper.exception.member.MemberDuplicateException;
import umc.healthper.exception.member.MemberNotFoundByIdException;
import umc.healthper.exception.member.MemberNotFoundByKakaoKeyException;
import umc.healthper.repository.MemberBlockRepository;
import umc.healthper.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberBlockRepository memberBlockRepository;

    /**
     * 회원 등록
     *
     * @param member 등록할 회원 객체
     * @return 등록한 회원의 id(memberId)
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원 목록 조회
     *
     * @return 조회한 회원 목록 List
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * memberId(PK)를 사용하여 회원 조회
     *
     * @param memberId 조회할 회원의 id
     * @return 조회한 Member 객체
     * @throws MemberNotFoundByIdException 조회할 Member가 없는 경우
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundByIdException::new);
    }

    /**
     * kakaoKey를 사용하여 회원 조회
     *
     * @param kakaoKey 조회할 회원의 kakaoKey
     * @return 조회한 Member 객체
     * @throws MemberNotFoundByKakaoKeyException 조회할 Member가 없는 경우
     */
    public Member findByKakaoKey(Long kakaoKey) {
        return memberRepository.findByKakaoKey(kakaoKey)
                .orElseThrow(MemberNotFoundByKakaoKeyException::new);
    }

    /**
     * 회원 차단
     *
     * @param loginMemberId   로그인 Member의 id (주체)
     * @param blockedMemberId 차단할 Member의 id (대상)
     * @throws MemberNotFoundByIdException   전달받은 id에 해당하는 Member가 존재하지 않는 경우
     * @throws MemberBlockDuplicateException 이미 차단한 Member인 경우
     */
    public void blockMember(Long loginMemberId, Long blockedMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundByIdException::new);
        Member blockedMember = memberRepository.findById(blockedMemberId)
                .orElseThrow(MemberNotFoundByIdException::new);

        validateDuplicateBlockedMember(loginMember, blockedMember);

        MemberBlock memberBlock = MemberBlock.createMemberBlock(loginMember, blockedMember);
        memberBlockRepository.save(memberBlock);
    }

    /**
     * 차단했던 회원의 차단 취소
     *
     * @param loginMemberId   로그인 Member (주체)
     * @param blockedMemberId 차단했던 Member (대상)
     * @throws MemberNotFoundByIdException  전달받은 id에 해당하는 Member가 존재하지 않는 경우
     * @throws MemberBlockNotFoundException 차단한 적 없는 Member인 경우
     */
    public void deleteBlockedMember(Long loginMemberId, Long blockedMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundByIdException::new);
        Member blockedMember = memberRepository.findById(blockedMemberId)
                .orElseThrow(MemberNotFoundByIdException::new);

        MemberBlock memberBlock = memberBlockRepository.findByMemberAndBlockedMember(loginMember, blockedMember)
                .orElseThrow(MemberBlockNotFoundException::new);

        loginMember.getMemberBlocks().remove(memberBlock);
        memberBlockRepository.delete(memberBlock);
    }

    /**
     * 이미 등록된 회원인지 중복 가입 여부 검증
     *
     * @param member 등록할 Member
     * @throws MemberDuplicateException 같은 kakaoKey로 이미 등록된 회원이 존재하는 경우
     */
    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByKakaoKey(member.getKakaoKey());
        if (findMember.isPresent()) {
            throw new MemberDuplicateException("이미 등록된 회원입니다.");
        }
    }

    /**
     * 이미 차단한 회원인지 중복 차단 여부 검증
     *
     * @param member        로그인 Member (주체)
     * @param blockedMember 차단할 Member (대상)
     * @throws MemberBlockDuplicateException 이미 차단한 Member인 경우
     */
    private void validateDuplicateBlockedMember(Member member, Member blockedMember) {
        for (MemberBlock memberBlock : member.getMemberBlocks()) {
            if (memberBlock.getBlockedMember() == blockedMember) {
                throw new MemberBlockDuplicateException("이미 차단한 회원입니다.");
            }
        }
    }
}
