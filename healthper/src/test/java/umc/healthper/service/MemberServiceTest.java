package umc.healthper.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import umc.healthper.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    //@Test
    public void 회원_등록() throws Exception {
        // given
        Member member = Member.createMember(100L, "우기");
        memberService.join(member);

        // when
        Member findMember = memberService.findById(member.getId());

        // then
        assertThat(findMember).isEqualTo(member);
    }

    //@Test(expected = IllegalStateException.class)
    public void 회원_중복_등록() throws Exception {
        // given
        Member member1 = Member.createMember(100L, "우기");
        memberService.join(member1);

        // when
        Member member2 = Member.createMember(100L, "피터");
        memberService.join(member2);

        // then
        fail("중복 회원이 저장되기 때문에 exception이 발생해야 한다.");
    }

    //@Test
    public void 회원_목록_조회() throws Exception {
        // given
        Member member1 = Member.createMember(100L, "회원1");
        memberService.join(member1);
        Member member2 = Member.createMember(101L, "회원2");
        memberService.join(member2);
        Member member3 = Member.createMember(102L, "회원3");
        memberService.join(member3);
        Member member4 = Member.createMember(103L, "회원4");
        memberService.join(member4);

        // when
        List<Member> members = memberService.findMembers();

        // then
        assertThat(members.size()).isEqualTo(4);
    }
}