package com.example.myvers.controller;

import com.example.myvers.domain.Friend;
import com.example.myvers.domain.Gender;
import com.example.myvers.domain.Grade;
import com.example.myvers.domain.Member;
import com.example.myvers.service.FriendService;
import com.example.myvers.service.MemberService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final MemberService memberService;

    // 로그인후 첫 화면, 친구 전체 목록.
    @GetMapping("/friend/{memberId}")
    public String friendList(@PathVariable Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        List<Friend> friends = member.getFriends();
        model.addAttribute("friends", friends);
        return "friend/friends";
    }

    // 친구 생성 화면.
    @GetMapping("/friend/{memberId}/new")
    public String createFriendForm(@PathVariable Long memberId, Model model) {
        // FriendForm 빌더 패턴으로 만들고 생성자는 protected이지만, 같은 패지키 안에 있기 때문에 사용가능.
        model.addAttribute("friendForm", new FriendForm());

        // 친구 생성 최대 수 검증
        try {
            validateCurrentFriendsNumber(memberId);
        } catch(IllegalStateException e) {
            // 오류 페이지로 넘기지 않고 뭔가.. 모달창 띄우게 만들고 싶은데. .이거 자바스크립트 배우긴 배워야할듯..
//            String message = "최대 친구 수를 초과하였습니다. 기존의 친구를 삭제하거나, 멤버쉽에 가입하여 최대 친구 수를 4명까지 늘릴 수 있습니다.";
//            model.addAttribute("message", message);
            return "friend/makeFriendFail";
        }
        return "friend/makeFriend";
    }
    // 친구 생성 최대 수 검증
    private void validateCurrentFriendsNumber(Long memberId) {
        Member member = memberService.findOne(memberId);
        if(member.getGrade() == Grade.NORMAL && member.getFriends().size() >= 2) {
            throw new IllegalStateException("최대 생성 친구 수를 초과하였습니다. VIP로 업그레이드 시 최대 4명까지 생성할 수 있습니다.");
        }
        if(member.getGrade() == Grade.VIP && member.getFriends().size() >= 4) {
            throw new IllegalStateException("최대 생성 친구 수를 초과하였습니다.");
        }
    }

    // 친구 생성
    @PostMapping("/friend/{memberId}/new")
    public String createFriend(@PathVariable Long memberId, @Valid @ModelAttribute FriendForm friendForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "friend/makeFriend";
        }
        Member member = memberService.findOne(memberId);

        Friend friend = Friend.builder()
                .age(friendForm.getAge())
                .name(friendForm.getName())
                .mbti(friendForm.getMbti())
                .gender(friendForm.getGender())
                .createdDate(LocalDateTime.now())
                .imageName(generateRandomImageName(friendForm.getGender(), member))
                .member(member)
                .build();

        friendService.generateFriend(friend);

        return "redirect:/friend/" + memberId;
    }


    private String generateRandomImageName(Gender gender, Member member) {
        Random random = new Random();
        String imageName;
        do {
            int imageNum = random.nextInt(10) + 1; // 1~10 까지 랜덤값 생성
            if(gender == Gender.MALE) {
                imageName = "/image/m" + imageNum+".jpeg";
            } else {
                imageName = "/image/f" + imageNum+".jpeg";
            }
        } while(isFail(member, imageName));
        return imageName;
    }
    private boolean isFail(Member member, String imageName) {
        List<Friend> friends = member.getFriends();
        for (Friend saveFriend : friends) {
            if (saveFriend.getImageName() != null && saveFriend.getImageName().equals(imageName)) {
                return true;
            }
        }
        return false;
    }


    // 친구 삭제
    @GetMapping("/friend/{memberId}/{friendId}/delete")
    public String friendDeleteForm(@PathVariable Long memberId, @PathVariable Long friendId, Model model) {
        addAttributeToModel(friendId, model);
        model.addAttribute("friendDeleteForm", FriendDeleteForm.builder().build());
        return "friend/delete";
    }


    @PostMapping("/friend/{memberId}/{friendId}/delete")
    public String friendDelete(@PathVariable Long memberId, @PathVariable Long friendId, @Valid FriendDeleteForm friendDeleteForm, BindingResult bindingResult, Model model) {
        addAttributeToModel(friendId, model);
        if(bindingResult.hasErrors()) {
            return "friend/delete";
        }
        // 비밀번호 검사
        Member member = memberService.findOne(memberId);
        if(!member.getPassword().equals(friendDeleteForm.getPassword())) {
            bindingResult.reject("password.notSame.originPassword", "비밀번호가 틀렸습니다.");
            return "friend/delete";
        }

        friendService.delete(friendId);
        return "redirect:/friend/" + memberId;
    }

    private void addAttributeToModel(Long friendId, Model model) {
        Friend friend = friendService.findById(friendId);
        String imageName = friend.getImageName();
        String name = friend.getName();
        String [] warning = {"주의!! " + name + "님의 존재가", "MyVers 우주에서 영원히 사라지게 됩니다!!"};
        model.addAttribute("imageName", imageName);
        model.addAttribute("name", name);
        model.addAttribute("warning", warning);
    }
}
