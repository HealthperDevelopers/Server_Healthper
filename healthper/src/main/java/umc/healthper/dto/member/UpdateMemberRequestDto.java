package umc.healthper.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class UpdateMemberRequestDto {

    @NotBlank
    private String nickname;
}
