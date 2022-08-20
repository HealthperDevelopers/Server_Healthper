package umc.healthper.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import umc.healthper.domain.member.MemberStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {

    private Long memberId;
    private String nickName;
    private MemberStatus status;
}
