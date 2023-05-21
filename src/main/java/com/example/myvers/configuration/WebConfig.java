package com.example.myvers.configuration;

import com.example.myvers.interceptor.AdminCheckInterceptor;
import com.example.myvers.interceptor.LoginCheckInterceptor;
import com.example.myvers.interceptor.PermissionCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /** 비로그인 회원 접근 거르기 */
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/image/*" ,"/error", "/login", "/logout", "/", "/member/new");

        /**  admin 페이지 접근 거르기 */
        registry.addInterceptor(new AdminCheckInterceptor())
                .order(2)
                .addPathPatterns("/members");

        /** 회원 본인의 인스턴스 이외의 접근 거르기
         * - "/member/{memberId}" : 회원정보
         * - "/member/{memberId}/edit" : 회원정보수정
         * - "/friend/{memberId}" : 친구목록 - 로그인 후 화면
         * - "/friend/{memberId}/new" : 친구생성
         * - "/friend/{memberId}/{friendId}/delete" : 친구삭제
         * - "/chat/{memberId}/{friendId}" : 대화창
         */
        registry.addInterceptor(new PermissionCheckInterceptor())
                .order(3)
                .addPathPatterns("/member/**", "/friend/**", "/chat/**")
                .excludePathPatterns("/member/new");
    }

}
