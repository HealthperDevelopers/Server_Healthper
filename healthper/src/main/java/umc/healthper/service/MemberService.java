package umc.healthper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberStatus;
import umc.healthper.dto.member.UpdateMemberRequestDto;
import umc.healthper.exception.member.*;
import umc.healthper.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 등록
     *
     * @param kakaoKey 가입하려는 회원의 kakaoKey
     * @param nickname 사용하고자 하는 닉네임
     * @return 등록된 회원의 id
     * @throws MemberNicknameDuplicateException 사용하려고 하는 닉네임이 이미 존재하는 경우
     * @throws MemberDuplicateException         전달받은 kakaoKey로 이미 등록된, 탈퇴하지 않은 회원이 존재하는 경우
     */
    @Transactional
    public Long joinMember(Long kakaoKey, String nickname) {
        validateDuplicateNickname(nickname);

        Optional<Member> optionalMember = memberRepository.findByKakaoKey(kakaoKey);

        Member member;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            validateAlreadyExistMember(member);
            member.rejoin(nickname);
        } else {
            member = Member.createMember(kakaoKey, nickname);
            memberRepository.save(member);
        }
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
     * @throws MemberNotFoundException 조회할 Member가 없는 경우
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    /**
     * kakaoKey를 사용하여 회원 조회
     *
     * @param kakaoKey 조회할 회원의 kakaoKey
     * @return 조회한 Member 객체
     * @throws MemberNotFoundException 조회할 Member가 없는 경우
     */
    public Member findByKakaoKey(Long kakaoKey) {
        return memberRepository.findByKakaoKey(kakaoKey)
                .orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 회원 정보 수정
     *
     * @param loginMemberId 수정할 Member
     * @param updateDto     수정할 내용이 담긴 DTO
     */
    @Transactional
    public void updateMember(Long loginMemberId, UpdateMemberRequestDto updateDto) {
        String nickname = updateDto.getNickname();

        validateDuplicateNickname(nickname);

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundException::new);
        loginMember.update(nickname);
    }

    /**
     * kakaoKey에 해당하는 회원 탈퇴
     * DB에서 실제로 삭제되지는 않고 status=RESIGNED로 변경됨
     *
     * @param loginMemberId 로그인 Member의 id
     * @throws MemberNotFoundException        id에 해당하는 회원이 없는 경우
     * @throws AlreadyResignedMemberException 이미 탈퇴한 회원인 경우
     */
    @Transactional
    public void deleteMember(Long loginMemberId) {
        Member member = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundException::new);
        validateAlreadyResignedMember(member);
        member.delete();
    }

    /**
     * 이미 사용중인 닉네임인지 검증
     *
     * @param nickname 사용하고자 하는 닉네임
     * @throws MemberNicknameDuplicateException 이미 사용중인 닉네임인 경우
     */
    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNicknameIs(nickname)) {
            throw new MemberNicknameDuplicateException("이미 존재하는 닉네임입니다.");
        }
    }

    /**
     * 이미 탈퇴한 회원인지 검증
     *
     * @param member 탈퇴하려는 회원
     * @throws AlreadyResignedMemberException 이미 탈퇴한 회원인 경우
     */
    private void validateAlreadyResignedMember(Member member) {
        if (member.getStatus() == MemberStatus.RESIGNED) {
            throw new AlreadyResignedMemberException("이미 탈퇴한 회원입니다.");
        }
    }

    /**
     * 이미 존재하는 회원인지 검증
     *
     * @param member 검증하고자 하는 회원
     * @throws MemberDuplicateException 이미 존재하는 회원인 경우
     */
    private void validateAlreadyExistMember(Member member) {
        if (member.getStatus() != MemberStatus.RESIGNED) {
            throw new MemberDuplicateException("이미 가입된 회원입니다.");
        }
    }
}
