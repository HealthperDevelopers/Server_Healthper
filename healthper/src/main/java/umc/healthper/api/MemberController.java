package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.healthper.dto.member.CreateMemberRequestDto;
import umc.healthper.dto.member.MemberResponseDto;
import umc.healthper.dto.member.UpdateMemberRequestDto;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.MemberService;

import javax.validation.Valid;

@Tag(name = "Member", description = "회원 API")
@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입",
            description = "가입할 회원의 정보(`kakaoKey`, `nickname`)을 받아 회원가입을 진행합니다.")
    @PostMapping("/member")
    public void joinMember(@RequestBody @Valid CreateMemberRequestDto requestDto) {
        memberService.joinMember(requestDto.getKakaoKey(), requestDto.getNickname());
    }

    @Operation(summary = "회원 조회",
            description = "`kakaoKey`에 해당하는 회원의 정보를 조회한다.\n\n" +
                    "(필요 없으시면 안쓰셔도 되고 요구사항 있으시면 말해주세요)")
    @GetMapping("/member")
    public MemberResponseDto getMember(@RequestParam Long kakaoKey) {
        return new MemberResponseDto(memberService.findByKakaoKey(kakaoKey));
    }

    @Operation(summary = "회원 정보 수정",
            description = "로그인 사용자의 정보를 수정한다. (현재 닉네임 수정만 가능)")
    @PutMapping("/member")
    public void updateMember(@Parameter(hidden = true) @Login Long loginMemberId,
                             @RequestBody @Valid UpdateMemberRequestDto requestDto) {
        memberService.updateMember(loginMemberId, requestDto);
    }

    @Operation(summary = "회원 탈퇴",
            description = "로그인 사용자가 탈퇴한다.\n\n" +
                    "실제로 DB에서 데이터가 삭제되지는 않고 탈퇴한 상태(`status=RESIGNED`)로 변경한다.")
    @DeleteMapping("/member")
    public void deleteMember(@Parameter(hidden = true) @Login Long loginMemberId) {
        memberService.deleteMember(loginMemberId);
    }


    @Operation(summary = "회원 차단",
            description = "`blockedMemberId`에 해당하는 회원을 차단합니다.")
    @PostMapping("/block/{blockedMemberId}")
    public void blockMember(@PathVariable Long blockedMemberId,
                            @Parameter(hidden = true) @Login Long loginMemberId) {
        memberService.blockMember(loginMemberId, blockedMemberId);
    }

    @Operation(summary = "회원 차단 취소",
            description = "`blockedMemberId`에 해당하는 회원의 차단을 취소한다.")
    @DeleteMapping("/block/{blockedMemberId}")
    public void cancelBlockedMember(@PathVariable Long blockedMemberId,
                                    @Parameter(hidden = true) @Login Long loginMemberId) {
        memberService.deleteBlockedMember(loginMemberId, blockedMemberId);
    }
}
