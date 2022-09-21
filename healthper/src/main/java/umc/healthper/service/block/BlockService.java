package umc.healthper.service.block;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.block.MemberBlock;
import umc.healthper.exception.block.MemberBlockDuplicateException;
import umc.healthper.exception.block.MemberBlockNotFoundException;
import umc.healthper.exception.member.MemberNotFoundException;
import umc.healthper.repository.block.MemberBlockRepository;
import umc.healthper.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlockService {

    private final MemberRepository memberRepository;
    private final MemberBlockRepository memberBlockRepository;

    /**
     * 회원 차단
     *
     * @param loginMemberId   로그인 Member의 id (주체)
     * @param blockedMemberId 차단할 Member의 id (대상)
     * @throws MemberNotFoundException       전달받은 id에 해당하는 Member가 존재하지 않는 경우
     * @throws MemberBlockDuplicateException 이미 차단한 Member인 경우
     */
    public void blockMember(Long loginMemberId, Long blockedMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundException::new);
        Member blockedMember = memberRepository.findById(blockedMemberId)
                .orElseThrow(MemberNotFoundException::new);

        validateDuplicateBlockedMember(loginMember, blockedMember);

        MemberBlock memberBlock = MemberBlock.createMemberBlock(loginMember, blockedMember);
        memberBlockRepository.save(memberBlock);
    }

    /**
     * 차단했던 회원의 차단 취소
     *
     * @param loginMemberId   로그인 Member (주체)
     * @param blockedMemberId 차단했던 Member (대상)
     * @throws MemberNotFoundException      전달받은 id에 해당하는 Member가 존재하지 않는 경우
     * @throws MemberBlockNotFoundException 차단한 적 없는 Member인 경우
     */
    public void deleteBlockedMember(Long loginMemberId, Long blockedMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(MemberNotFoundException::new);
        Member blockedMember = memberRepository.findById(blockedMemberId)
                .orElseThrow(MemberNotFoundException::new);

        MemberBlock memberBlock = memberBlockRepository.findByMemberAndBlockedMember(loginMember, blockedMember)
                .orElseThrow(MemberBlockNotFoundException::new);

        loginMember.getMemberBlocks().remove(memberBlock);
        memberBlockRepository.delete(memberBlock);
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
