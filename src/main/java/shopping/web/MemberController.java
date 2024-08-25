package shopping.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shopping.domain.Member;
import shopping.domain.dto.MemberDto;
import shopping.service.MemberService;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public TokenResponse register(@Validated @RequestBody MemberDto registerParam, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.info(bindingResult.getAllErrors().toString());
            throw new ValidationException(bindingResult.getFieldError().getCode());
        }

        Member registeredMember = memberService.register(registerParam.toEntity());

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성|
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_MEMBER, registeredMember);
        log.info("session = {}", session);


        return new TokenResponse(session.getId());
    }

    @PostMapping("/login")
    public TokenResponse login(@Validated @RequestBody MemberDto loginParam, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info(bindingResult.getAllErrors().toString());
            throw new ValidationException(bindingResult.getFieldError().getCode());
        }

        Optional<Member> loginMember = memberService.login(loginParam.getEmail(), loginParam.getPassword());
        if (loginMember.isPresent()) {
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.get());

            return new TokenResponse(session.getId());
        }
        return new TokenResponse("login fail");
    }

    record TokenResponse(String token){}
}
