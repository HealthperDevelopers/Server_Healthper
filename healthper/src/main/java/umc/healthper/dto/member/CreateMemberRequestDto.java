package umc.healthper.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateMemberRequestDto {
    private Long kakaoKey;
    private String nickname;
}
