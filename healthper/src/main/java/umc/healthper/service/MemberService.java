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
     * @throws MemberNicknameDuplicateException 사용하려고 하는 닉네임이 이미 존재하는 경우
     * @throws MemberDuplicateException         같은 kakaoKey로 이미 등록된 회원이 존재하는 경우
     */
    @Transactional
    public void joinMember(Long kakaoKey, String nickname) {
        validateDuplicateMemberNickname(nickname);

        Optional<Member> optionalFindMember = memberRepository.findByKakaoKey(kakaoKey);
        if (optionalFindMember.isPresent()) {
            Member findMember = optionalFindMember.get();
            if (findMember.getStatus() == MemberStatus.RESIGNED) {
                findMember.rejoin(nickname);
            }
            throw new MemberDuplicateException("이미 가입된 회원입니다.");
        }

        Member member = Member.createMember(kakaoKey, nickname);
        memberRepository.save(member);
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
     * 회원 정보 수정
     *
     * @param loginMemberId 수정할 Member
     * @param updateDto     수정할 내용이 담긴 DTO
     */
    public void updateMember(Long loginMemberId, UpdateMemberRequestDto updateDto) {
        String nickname = updateDto.getNickname();

        validateDuplicateMemberNickname(nickname);

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundByIdException::new);
        loginMember.update(nickname);
    }

    /**
     * kakaoKey에 해당하는 회원 탈퇴
     * DB에서 실제로 삭제되지는 않고 status=RESIGNED로 변경됨
     *
     * @param loginMemberId 로그인 Member의 id
     * @throws MemberNotFoundByIdException    id에 해당하는 회원이 없는 경우
     * @throws AlreadyResignedMemberException 이미 탈퇴한 회원인 경우
     */
    public void deleteMember(Long loginMemberId) {
        Member member = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundByIdException::new);
        validAlreadyResignedMember(member);
        member.delete();
    }

    /**
     * 이미 사용중인 닉네임인지 검증
     *
     * @param nickname 사용하고자 하는 닉네임
     * @throws MemberNicknameDuplicateException 이미 사용중인 닉네임인 경우
     */
    private void validateDuplicateMemberNickname(String nickname) {
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
    private void validAlreadyResignedMember(Member member) {
        if (member.getStatus() == MemberStatus.RESIGNED) {
            throw new AlreadyResignedMemberException("이미 탈퇴한 회원입니다.");
        }
    }
}
