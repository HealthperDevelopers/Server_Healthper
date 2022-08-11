package umc.healthper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import umc.healthper.global.argumentresolver.Login;
import umc.healthper.global.login.SessionConst;
import umc.healthper.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/home")
    @ResponseBody
    public String home(@Login Long userId){
        if(userId == null)
            return "로그인 해주세요.";
        return "로그인 완료";
    }

    @GetMapping("/login")
    public String login(@RequestParam(defaultValue = "-1") Long kakaoId,
                        @RequestParam(defaultValue = "/record/calender") String redirectURL, HttpServletRequest request) throws JsonProcessingException {
        Long kakaoKey = kakaoId;

        if(kakaoKey == null || kakaoKey == -1){
            return "redirect:/home";
        }
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        Long userId = kakaoKey;//userService.getId(kakaoKey);//카카오 키에 해당 되는 유저 아이디 가져외
        session.setAttribute(SessionConst.LOGIN_MEMBER, userId);

        LocalDate now = LocalDate.now();
        return "redirect:"+redirectURL+"?year="+now.getYear()+"&month="+now.getMonthValue();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/home";
    }
}
