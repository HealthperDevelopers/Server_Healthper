package umc.healthper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberStatus;
import umc.healthper.exception.member.MemberNotFoundException;
import umc.healthper.global.login.SessionConst;
import umc.healthper.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인/로그아웃 API")
public class LoginController {

    private final MemberService memberService;

    @Operation(summary = "Login",
            description = "kakaoId(kakaoKey)를 전달받아 로그인 합니다.\n\n" +
                    "DB에 존재하지 않는(가입한 적 없는) 회원 또는 탈퇴했던 회원인 경우에는 404 Error 응답")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = List.class)))})
    @GetMapping("/login")
    public String login(@RequestParam(name = "kakaoId") Long kakaoKey,
                        @Parameter(hidden = true) @RequestParam(defaultValue = "/record/calender") String redirectURL,
                        HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();

        // Session에 로그인 회원 데이터 보관
        // DB에 존재하지 않는 회원 또는 탈퇴했던 회원인 경우에는 404 Error 응답
        Member member = memberService.findByKakaoKey(kakaoKey);
        if (member.getStatus() == MemberStatus.RESIGNED) {
            throw new MemberNotFoundException();
        }
        session.setAttribute(SessionConst.LOGIN_MEMBER, member.getId());

        LocalDate now = LocalDate.now();
        return "redirect:" + redirectURL + "?year=" + now.getYear() + "&month=" + now.getMonthValue();
    }

    @Operation(summary = "Logout", description = "세션에서 MEMBER 정보를 제거합니다.")
    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
