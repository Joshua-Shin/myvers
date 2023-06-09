package com.example.myvers.controller;

import com.example.myvers.configuration.FriendConst;
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

    /** 로그인후 첫 화면, 친구 전체 목록.*/
    @GetMapping("/friend/{memberId}")
    public String friendList(@PathVariable Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        List<Friend> friends = member.getFriends(); // 이게 프록시로 들어올테니까..
        model.addAttribute("friends", friends); // 프록시 상태로 view로 넘겨버리네. 그러면 view쪽에서 friend.getName() 할떄마다 쿼리 나가는거야.
        // 여기도 friends for문 돌릴떄, 페치조인해서 한번에 가져오게 해야돼.
        return "friend/friends";
    }

    /** 친구 생성 화면. */
    @GetMapping("/friend/{memberId}/new")
    public String createFriendForm(@PathVariable Long memberId, Model model) {
        // FriendForm 빌더 패턴으로 만들고 생성자는 protected이지만, 같은 패지키 안에 있기 때문에 사용가능.
        model.addAttribute("friendForm", new FriendForm());

        /** 친구 생성 최대 수 검증 */
        try {
            validateCurrentFriendsNumber(memberId);
        } catch(IllegalStateException e) {
            return "friend/makeFriendFail";
        }
        return "friend/makeFriend";
    }
    private void validateCurrentFriendsNumber(Long memberId) {
        Member member = memberService.findOne(memberId);
        if(member.getGrade() == Grade.NORMAL && member.getFriends().size() >= 2) {
            throw new IllegalStateException("최대 생성 친구 수를 초과하였습니다. VIP로 업그레이드 시 최대 4명까지 생성할 수 있습니다.");
        }
        if(member.getGrade() == Grade.VIP && member.getFriends().size() >= 4) {
            throw new IllegalStateException("최대 생성 친구 수를 초과하였습니다.");
        }
    }

    /** 친구 생성 */
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
        int imageNum = random.nextInt(10) + 1; // 1~10 까지 랜덤값 생성
        do {
            if(gender == Gender.MALE) {
                imageName = "/image/m" + imageNum+".jpeg";
            } else {
                imageName = "/image/f" + imageNum+".jpeg";
            }
            imageNum++;
            if(imageNum == FriendConst.MAXIMUM_NUMBER + 1) {
                imageNum = 1;
            }

          /** 친구 이미지 중복 검사 */
        } while(isDuplicate(member, imageName));
        return imageName;
    }
    private boolean isDuplicate(Member member, String imageName) {
        return member.getFriends().stream()
                .anyMatch(saveFriend -> saveFriend.getImageName() != null && saveFriend.getImageName().equals(imageName));
    }


    /** 친구 삭제 폼 */
    @GetMapping("/friend/{memberId}/{friendId}/delete")
    public String friendDeleteForm(@PathVariable Long memberId, @PathVariable Long friendId, Model model) {
        addAttributeToModel(friendId, model);
        model.addAttribute("friendDeleteForm", FriendDeleteForm.builder().build());
        return "friend/delete";
    }

    /** 친구 삭제 */
    @PostMapping("/friend/{memberId}/{friendId}/delete")
    public String friendDelete(@PathVariable Long memberId, @PathVariable Long friendId, @Valid FriendDeleteForm friendDeleteForm, BindingResult bindingResult, Model model) {
        addAttributeToModel(friendId, model);
        if(bindingResult.hasErrors()) {
            return "friend/delete";
        }
        /** 비밀번호 검사 */
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
