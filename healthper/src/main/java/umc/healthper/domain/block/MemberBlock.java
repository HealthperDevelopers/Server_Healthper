package umc.healthper.domain.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import umc.healthper.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBlock {

    @Id
    @GeneratedValue
    @Column(name = "member_block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_member_id")
    private Member blockedMember;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    //== 생성 메서드 ==//
    public static MemberBlock createMemberBlock(Member member, Member blockedMember) {
        MemberBlock memberBlock = new MemberBlock();
        memberBlock.setMember(member);
        memberBlock.setBlockedMember(blockedMember);
        member.getMemberBlocks().add(memberBlock);
        return memberBlock;
    }

    //== Setter ==//
    private void setMember(Member member) {
        this.member = member;
    }

    private void setBlockedMember(Member blockedMember) {
        this.blockedMember = blockedMember;
    }
}
