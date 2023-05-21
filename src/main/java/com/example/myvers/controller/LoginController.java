package com.example.myvers.controller;

import com.example.myvers.configuration.SessionConst;
import com.example.myvers.domain.Member;
import com.example.myvers.service.LoginService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String home(LoginForm loginForm) {
        return "member/index";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "member/index";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if(loginMember == null) {
            bindingResult.reject("idPassword.notMatch", "아이디와 패스워드가 알맞지 않습니다.");
            return "member/index";
        }

        // 로그인 성공 처리.
        // 세션이 있으면 세션 반환, 없으면 만들어서 반환
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관 // 이미 로그인한 사용자인데 또 로그인 화면으로 와서 다른 계정으로 로그인 해버리면, 새로운 계정으로 세션값이 덮어지네.
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/friend/" + loginMember.getId();
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
