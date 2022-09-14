package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.MemberService;

@Tag(name = "Member", description = "회원 API")
@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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
