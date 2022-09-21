package umc.healthper.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.service.block.BlockService;

@Tag(name = "Block", description = "차단 API")
@RequestMapping("/block")
@RestController
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @Operation(summary = "회원 차단",
            description = "`targetMemberId`에 해당하는 회원을 차단합니다.")
    @PostMapping("/member/{targetMemberId}")
    public void blockMember(@PathVariable Long targetMemberId,
                            @Parameter(hidden = true) @Login Long loginMemberId) {
        blockService.blockMember(loginMemberId, targetMemberId);
    }

    @Operation(summary = "회원 차단 취소",
            description = "`blockedMemberId`에 해당하는 회원의 차단을 취소한다.")
    @DeleteMapping("/member/{blockedMemberId}")
    public void cancelBlockedMember(@PathVariable Long blockedMemberId,
                                    @Parameter(hidden = true) @Login Long loginMemberId) {
        blockService.deleteBlockedMember(loginMemberId, blockedMemberId);
    }
}
