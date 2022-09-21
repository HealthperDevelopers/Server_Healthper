package umc.healthper.dto.member;

import lombok.Getter;
import lombok.Setter;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberStatus;

@Getter
@Setter
public class MemberResponseDto {
    private Long kakaokey;
    private String nickname;
    private Integer reportedCount;
    private MemberStatus status;

    public MemberResponseDto(Member member) {
        this.kakaokey = member.getKakaoKey();
        this.nickname = member.getNickname();
        this.reportedCount = member.getReportedCount();
        this.status = member.getStatus();
    }
}
