package com.example.myvers.controller;

import com.example.myvers.configuration.Algorithm;
import com.example.myvers.configuration.MemberConst;
import com.example.myvers.configuration.TalkConst;
import com.example.myvers.domain.Friend;
import com.example.myvers.domain.Member;
import com.example.myvers.domain.Speaker;
import com.example.myvers.domain.Talk;
import com.example.myvers.service.FriendService;
import com.example.myvers.service.MemberService;
import com.example.myvers.service.messagegenerator.MessageGeneratorService;
import com.example.myvers.service.TalkService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TalkController {
    private final MemberService memberService;
    private final FriendService friendService;
    private final TalkService talkService;
    private final MessageGeneratorService messageGeneratorService;

    @GetMapping("/chat/{memberId}/{friendId}")
    public String showChatRoom(@PathVariable Long memberId, @PathVariable Long friendId, Model model) {
        addAttributeToModel(memberId, friendId, model);
        model.addAttribute("talkForm", TalkForm.builder().build());
        return "chat/chatRoom";
    }

    @PostMapping("/chat/{memberId}/{friendId}")
    public String processChat(@PathVariable Long memberId, @PathVariable Long friendId, @Valid TalkForm talkForm, BindingResult bindingResult, Model model) {
        addAttributeToModel(memberId, friendId, model);
        if(bindingResult.hasErrors()) {
            return "chat/chatRoom";
        }
        // 회원당 톡 개수 초과 검사 현재 100개로 할당.
        if(findTotalTalkCount(memberId) >= TalkConst.MAXIMUM_NUMBER) {
            bindingResult.reject("exceed.number.talk", TalkConst.EXCEED_MAXIMUM_NUMBER);
            return "chat/chatRoom";
        }

        Friend friend = friendService.findById(friendId);
        Talk talk = Talk.builder()
                .friend(friend)
                .speaker(Speaker.HUMAN)
                .message(talkForm.getMessage())
                .createdDateTime(LocalDateTime.now())
                .time(Algorithm.generateTimeSentence(LocalDateTime.now()))
                .build();
        talkService.save(talk);

        List<Talk> talks = friend.getTalks();

//        chatGPT 에게 응답 메시지 요청하기.
        String receivedMessage = messageGeneratorService.receiveMessage(talks);

        Talk talk2 = Talk.builder()
                .friend(friend)
                .speaker(Speaker.AI)
                .message(receivedMessage)
                .createdDateTime(LocalDateTime.now())
                .time(Algorithm.generateTimeSentence(LocalDateTime.now()))
                .build();
        talkService.save(talk2);

        return "redirect:/chat/" + memberId + "/" + friendId;
    }

    private long findTotalTalkCount(Long memberId) {
        Member member = memberService.findOne(memberId);
        List<Friend> friends = member.getFriends();
        long totalTalkCount = 0L;
        for (Friend friend : friends) {
            totalTalkCount += friend.getTalks().size();
        }
        return totalTalkCount;
    }

    private void addAttributeToModel(Long memberId, Long friendId, Model model) {
        Member member = memberService.findOne(memberId);
        List<Friend> friends = member.getFriends();
        model.addAttribute("friends", friends);
        Friend friend = friendService.findById(friendId);
        List<Talk> talks = friend.getTalks();
        model.addAttribute("talks", talks);
        model.addAttribute("friend", friend);
    }

//    @PostMapping("/chat/{memberId}/{friendId}")
    @ResponseBody
    public void async(@PathVariable Long memberId, @PathVariable Long friendId, String message, HttpServletResponse response) throws Exception {
        CompletableFuture.runAsync(() -> {
            // 첫번째 작업 수행
            Friend friend = friendService.findById(friendId);

            Talk talk = Talk.builder()
                    .friend(friend)
                    .speaker(Speaker.HUMAN)
                    .message(message)
                    .createdDateTime(LocalDateTime.now())
                    .time(Algorithm.generateTimeSentence(LocalDateTime.now()))
                    .build();
            talkService.save(talk);

            try {
                // 첫번째 리다이렉트 수행
                response.sendRedirect("/chat/" + memberId + "/" + friendId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).thenRun(() -> {
            // 두번째 작업 수행
            Friend friend = friendService.findById(friendId);
            List<Talk> talks = friend.getTalks();
            String receivedMessage = messageGeneratorService.receiveMessage(talks);

            Talk talk2 = Talk.builder()
                    .friend(friend)
                    .speaker(Speaker.AI)
                    .message(receivedMessage)
                    .createdDateTime(LocalDateTime.now())
                    .time(Algorithm.generateTimeSentence(LocalDateTime.now()))
                    .build();
            talkService.save(talk2);

            try {
                // 두번째 리다이렉트 수행
                response.sendRedirect("/chat/" + memberId + "/" + friendId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
