package umc.healthper.global.argumentresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import umc.healthper.domain.member.Member;
import umc.healthper.domain.member.MemberStatus;
import umc.healthper.exception.member.MemberNotFoundException;
import umc.healthper.global.login.SessionConst;
import umc.healthper.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgResolver implements HandlerMethodArgumentResolver {
    private MemberService memberService;
    public LoginMemberArgResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);//@Login어노테이션 붙어 있음?

        boolean hasId = Long.class.isAssignableFrom(parameter.getParameterType());//type이 멤버임?

        return hasLoginAnnotation && hasId;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        Long memberId = (Long)session.getAttribute(SessionConst.LOGIN_MEMBER);
        Member loginUser = memberService.findById(memberId);
        //log.info("user status: {}", loginUser.getStatus());
        if(loginUser.getStatus() != MemberStatus.NORMAL)
            throw new MemberNotFoundException();

        if(session == null){
            return null;
        }
        return memberId;
    }
}

