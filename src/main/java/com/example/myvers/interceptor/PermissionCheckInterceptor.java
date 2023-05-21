package com.example.myvers.interceptor;

import com.example.myvers.configuration.SessionConst;
import com.example.myvers.domain.Member;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class PermissionCheckInterceptor implements HandlerInterceptor {

    /**
     * - 아래 url에 접근할 수 있는 권한이 있는지. 없다면 잘못된 요청이라는 안내 해주기.
     * - "/member/{memberId}" : 회원정보
     * - "/member/{memberId}/edit" : 회원정보수정
     * - "/friend/{memberId}" : 친구목록 - 로그인 후 화면
     * - "/friend/{memberId}/new" : 친구생성
     * - "/friend/{memberId}/{friendId}/delete" : 친구삭제
     * - "/chat/{memberId}/{friendId}" : 대화창
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        // 이것 때문이라도, 딱 저 위에 pathVariable이 들어오는 애들만 넣어야돼.
        String[] pathSegments = extractPathVariable(requestURI);
        long memberId = Long.parseLong(pathSegments[2]);

        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if(member.getId() != memberId) {
            response.sendRedirect("/error/" + 403);
            return false;
        }
        return true;
    }
    private String[] extractPathVariable(String requestURI) {
        String[] pathSegments = requestURI.split("/");
        return pathSegments;
    }
}








