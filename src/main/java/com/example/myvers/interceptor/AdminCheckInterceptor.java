package com.example.myvers.interceptor;

import com.example.myvers.configuration.SessionConst;
import com.example.myvers.domain.Grade;
import com.example.myvers.domain.Member;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(member.getGrade()!= Grade.ADMIN) {
            response.sendRedirect("/error/" + 404);
            return false;
        }
        return true;
    }
}
