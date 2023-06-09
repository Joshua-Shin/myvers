package com.example.myvers.controller;

import com.example.myvers.configuration.Algorithm;
import com.example.myvers.configuration.MemberConst;
import com.example.myvers.domain.Friend;
import com.example.myvers.domain.Grade;
import com.example.myvers.domain.Member;
import com.example.myvers.service.MemberService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /** 회원 가입 폼 */
    @GetMapping("/member/new")
    public String signupForm(MemberForm memberForm, BindingResult bindingResult) {
        // 회원 정원 초과 검사
        if(memberService.findAll().size() >= MemberConst.MAXIMUM_NUMBER) {
            bindingResult.reject("exceed.number", MemberConst.EXCEED_MAXIMUM_NUMBER);
        }
        return "member/join";
    }

    /** 회원 가입 */
    @PostMapping("/member/new")
    public String signup(@Valid @ModelAttribute MemberForm memberForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "member/join";
        }
        // 회원 정원 초과 검사
        if(memberService.findAll().size() >= MemberConst.MAXIMUM_NUMBER) {
            bindingResult.reject("exceed.number", MemberConst.EXCEED_MAXIMUM_NUMBER);
            return "member/join";
        }

        // 아이디 중복 검사
        if(memberService.findByLoginId(memberForm.getLoginId()).isPresent()) {
            bindingResult.reject("loginId.notUnique", MemberConst.NOT_UNIQUE_ID);
            return "member/join";
        }

        // 이메일 중복 검사
        if(memberService.findByLoginId(memberForm.getLoginId()).isPresent()) {
            bindingResult.reject("email.notUnique", MemberConst.NOT_UNIQUE_EMAIL);
            return "member/join";
        }

        // 비밀번호 검사
        if(!memberForm.getPassword().equals(memberForm.getConfirmPassword())) {
            bindingResult.reject("password.notSame", MemberConst.NOT_MATCH_CONFIRM_PASSWORD);
            return "member/join";
        }

        Member member = Member.builder()
                .email(memberForm.getEmail())
                .grade(Grade.NORMAL)
                .password(memberForm.getPassword())
                .loginId(memberForm.getLoginId())
                .name(memberForm.getName())
                .createdDate(LocalDateTime.now())
                .build();
        memberService.join(member);
        return "redirect:/";
    }

    /** 회원 정보 조회 */
    @GetMapping("/member/{memberId}")
    public String info(@PathVariable Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        String joinDate = Algorithm.generateTimeSentence(member.getCreatedDate());
        model.addAttribute("member", member);
        model.addAttribute("joinDate", joinDate);
        return "member/memberInfo";
    }

    /** 회원 정보 수정 폼 */
    @GetMapping("/member/{memberId}/edit")
    public String editForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        MemberModifyForm memberModifyForm = MemberModifyForm.builder()
                .email(member.getEmail())
                .name(member.getName())
                .loginId(member.getLoginId())
                .build();
        model.addAttribute("memberModifyForm", memberModifyForm);
        return "member/edit";
    }

    /** 회원 정보 수정 */
    @PostMapping("/member/{memberId}/edit")
    public String edit(@PathVariable Long memberId, @Valid @ModelAttribute MemberModifyForm memberModifyForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "member/edit";
        }
        Member member = memberService.findOne(memberId);
        // 기존 비밀번호와 수정폼에서 입력한 기존 비밀번호가 동일한지 확인
        if(!memberModifyForm.getPassword().equals(member.getPassword())) {
            bindingResult.reject("password.notSame.originPassword", MemberConst.NOT_MATCH_ORIGINAL_PASSWORD);
            return "member/edit";
        }
        // 바꿀 비밀번호와 바꿀 비밀번호 확인 비밀번호가 동일한지 확인.
        if(!memberModifyForm.getNewPassword().equals(memberModifyForm.getNewPasswordValid())) {
            bindingResult.reject("password.notSame", MemberConst.NOT_MATCH_CONFIRM_NEWPASSWORD_);
            return "member/edit";
        }

        memberService.update(memberId, memberModifyForm.getNewPassword(), memberModifyForm.getName(), memberModifyForm.getEmail());

        return "redirect:/member/" + memberId;
    }

    /** 전체 멤버 조회. admin 페이지 */
    @GetMapping("/members")
    @ResponseBody
    public Result<List<MemberDto>> members() {
        List<MemberDto> collect = new ArrayList<>();
        List<Member> members = memberService.findAll();
        for (Member member : members) {
            MemberDto memberDto = MemberDto.builder()
                    .loginId(member.getLoginId())
                    .id(member.getId())
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .createdDate(member.getCreatedDate())
                    .grade(member.getGrade())
                    .name(member.getName())
                    .build();
            List<Friend> friends = member.getFriends();
            for (Friend friend : friends) {
                Pair<Long, String> pair = Pair.of(friend.getId(), friend.getName());
                memberDto.getFriends().add(pair);
            }
            collect.add(memberDto);
        }
        return new Result<>(collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
