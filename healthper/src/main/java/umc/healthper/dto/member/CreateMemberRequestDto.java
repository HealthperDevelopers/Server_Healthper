package umc.healthper.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateMemberRequestDto {
    private Long kakaoKey;
    private String nickname;
}
