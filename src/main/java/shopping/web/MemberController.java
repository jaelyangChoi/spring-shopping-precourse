package shopping.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shopping.domain.Member;
import shopping.service.MemberService;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public String register(@Validated @RequestBody Member member, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.info(bindingResult.getAllErrors().toString());
            return "입력 값 오류";
        }

        try {
            Member registeredMember = memberService.register(member);
            //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, registeredMember);
            log.info("session = {}", session);
            return session.getId();
        } catch (RuntimeException e) {
            log.error("REGISTER ERROR", e);
            return e.getMessage();
        }
    }

    @PostMapping("/login")
    public String login(@Validated @RequestBody LoginDto loginParam, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info(bindingResult.getAllErrors().toString());
            return "입력 값 오류";
        }

        Optional<Member> loginMember = memberService.login(loginParam.getEmail(), loginParam.getPassword());
        if (loginMember.isPresent()) {
            //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.get());
            return session.getId();
        }
        return "로그인 실패";
    }
}
