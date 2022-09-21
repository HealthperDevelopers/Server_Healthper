package umc.healthper.service;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberStatus;
import umc.healthper.exception.member.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원 등록")
    public void joinMember() {
        // given
        memberService.joinMember(100L, "회원1");
        Member member = memberService.findByKakaoKey(100L);

        // when
        Member findMember = memberService.findById(member.getId());

        // then
        assertThat(findMember).isEqualTo(member);
    }

    @Test(expected = MemberDuplicateException.class)
    @DisplayName("동일한 kakaoKey로 회원 중복 등록")
    public void joinDuplicateMember() {
        // given
        memberService.joinMember(100L, "회원1");

        // when
        memberService.joinMember(100L, "회원2");

        // then
        fail("중복 회원이 저장되기 때문에 예외가 발생해야 한다.");
    }

    @Test(expected = MemberNicknameDuplicateException.class)
    @DisplayName("이미 사용중인 닉네임으로 회원 등록")
    public void joinDuplicateMemberNickname() {
        // given
        memberService.joinMember(100L, "회원");

        // when
        memberService.joinMember(101L, "회원");

        // then
        fail("이미 사용중인 닉네임을 사용하려고 했으므로 예외가 발생해야 한다.");
    }

    @Test
    @DisplayName("회원 목록 조회")
    public void findMembers() {
        // given
        memberService.joinMember(100L, "회원1");
        memberService.joinMember(101L, "회원2");
        memberService.joinMember(102L, "회원3");
        memberService.joinMember(103L, "회원4");

        // when
        List<Member> members = memberService.findMembers();

        // then
        assertThat(members.size()).isEqualTo(4);
    }

    @Test(expected = MemberNotFoundException.class)
    @DisplayName("존재하지 않는 id로 회원 조회")
    public void findByIdNotFound() {
        // when
        memberService.findById(1L);

        // then
        fail("존재하지 않는 회원 id이므로 예외가 발생한다.");
    }

    @Test
    @DisplayName("kakaoKey로 회원 조회")
    public void findByKakaoKey() {
        // given
        memberService.joinMember(100L, "회원");

        // when
        Member member = memberService.findByKakaoKey(100L);

        // then
        assertThat(member.getKakaoKey()).isEqualTo(100L);
    }

    @Test(expected = MemberNotFoundException.class)
    @DisplayName("존재하지 않는 kakaoKey로 회원 조회")
    public void findByKakaoKeyNotFound() {
        // when
        memberService.findByKakaoKey(100L);

        // then
        fail("존재하지 않는 kakaoKey이므로 예외가 발생한다.");
    }



    @Test
    @DisplayName("회원 탈퇴")
    public void deleteMember() {
        // given
        memberService.joinMember(100L, "회원");
        Member member = memberService.findByKakaoKey(100L);
        memberService.deleteMember(member.getId());

        // when
        Member findMember = memberService.findByKakaoKey(100L);

        // then
        assertThat(findMember.getStatus()).isEqualTo(MemberStatus.RESIGNED);
    }

    @Test(expected = AlreadyResignedMemberException.class)
    @DisplayName("이미 탈퇴한 회원을 탈퇴")
    public void deleteAlreadyResignedMember() {
        // given
        memberService.joinMember(100L, "회원");
        Member member = memberService.findByKakaoKey(100L);
        memberService.deleteMember(member.getId());

        // when
        memberService.deleteMember(member.getId());

        // then
        fail("이미 탈퇴한 회원이 탈퇴하려고 하였으므로 예외가 발생한다.");
    }
}