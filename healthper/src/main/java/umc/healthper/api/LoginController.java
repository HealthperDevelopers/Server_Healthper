package umc.healthper.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import umc.healthper.domain.login.LoginService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    @ResponseBody
    public String login(@RequestParam("code")String code) throws JsonProcessingException {
        Long kakaoId = loginService.kakoLogin(code);

        return "ok " + kakaoId;
    }
}
