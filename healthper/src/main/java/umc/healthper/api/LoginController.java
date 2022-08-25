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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import umc.healthper.dto.completeExercise.GetDetails;
import umc.healthper.exception.ExceptionResponse;
import umc.healthper.global.Swagger;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.global.login.SessionConst;
import umc.healthper.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인/로그아웃 API")
public class LoginController {

    private final MemberService memberService;

    @Swagger
    @Operation(summary = "임시 시작 페이지", description = "로그인 여부를 확인할 수 있습니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = String.class)))})
    @GetMapping("/home")
    @ResponseBody
    public String home(@Parameter(hidden = true)@Login Long userId) {
        if(userId == null)
            return "로그인 해주세요.";
        return "로그인 완료";
    }


    @Operation(summary = "Login", description = "kakaoId를 통해 로그인 합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))})
    @Swagger
    @GetMapping("/login")
    public String login(@RequestParam Long kakaoId,
                        @RequestParam(defaultValue = "/record/calender") String redirectURL, HttpServletRequest request) throws JsonProcessingException {
        Long kakaoKey = kakaoId;

        if(kakaoKey == null){
            return "redirect:/home";
        }
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        Long userId = memberService.findByKakaoKey(kakaoKey).getId();
        session.setAttribute(SessionConst.LOGIN_MEMBER, userId);

        LocalDate now = LocalDate.now();
        return "redirect:"+redirectURL+"?year="+now.getYear()+"&month="+now.getMonthValue();
    }

    @Operation(summary = "Logout", description = "세션에서 MEMBER 정보를 제거합니다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = Void.class)))})
    @Swagger
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/home";
    }
}
