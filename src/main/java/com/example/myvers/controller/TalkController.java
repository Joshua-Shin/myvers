package com.example.myvers.controller;

import com.example.myvers.configuration.Algorithm;
import com.example.myvers.configuration.TalkConst;
import com.example.myvers.domain.Friend;
import com.example.myvers.domain.Member;
import com.example.myvers.domain.Speaker;
import com.example.myvers.domain.Talk;
import com.example.myvers.service.FriendService;
import com.example.myvers.service.MemberService;
import com.example.myvers.service.messagegenerator.MessageGeneratorService;
import com.example.myvers.service.TalkService;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TalkController {
    private final MemberService memberService;
    private final FriendService friendService;
    private final TalkService talkService;
    private final MessageGeneratorService messageGeneratorService;

    /** 대화 방 */
    @GetMapping("/chat/{memberId}/{friendId}")
    public String showChatRoom(@PathVariable Long memberId, @PathVariable Long friendId, Model model) {
        addAttributeToModel(memberId, friendId, model);
        model.addAttribute("talkForm", TalkForm.builder().build());
        return "chat/chatRoom";
    }

    /** 메시지 보내기 */
    @PostMapping("/chat/{memberId}/{friendId}")
    public String processChat(@PathVariable Long memberId, @PathVariable Long friendId, @Valid TalkForm talkForm, BindingResult bindingResult, Model model) {
        addAttributeToModel(memberId, friendId, model);
        /** 메시지 입력 안했을때 필드 오류 */
        if(bindingResult.hasErrors()) {
            return "chat/chatRoom";
        }
        Member member = memberService.findOne(memberId);
        /** 회원당 톡 개수 초과 검사 현재 100개로 할당. */
        if(member.getTalkCount() >= TalkConst.MAXIMUM_NUMBER) {
            bindingResult.reject("exceed.number.talk", TalkConst.EXCEED_MAXIMUM_NUMBER);
            return "chat/chatRoom";
        }

        // 사용자가 입력한 메시지 저장
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

        /** chatGPT api 요청 에러 발생시 임시 메시지 저장 및 전달. */
        String receivedMessage;
        try{
            receivedMessage = messageGeneratorService.receiveMessage(talks);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            Talk talk2 = Talk.builder()
                    .friend(friend)
                    .speaker(Speaker.AI)
                    .message(TalkConst.CHATGPT_ERROR)
                    .createdDateTime(LocalDateTime.now())
                    .time(Algorithm.generateTimeSentence(LocalDateTime.now()))
                    .build();
            talkService.save(talk2);

            return "redirect:/chat/" + memberId + "/" + friendId;
        }

        // chatGPT가 전달한 메시지 저장.
        Talk talk2 = Talk.builder()
                .friend(friend)
                .speaker(Speaker.AI)
                .message(receivedMessage)
                .createdDateTime(LocalDateTime.now())
                .time(Algorithm.generateTimeSentence(LocalDateTime.now()))
                .build();
        talkService.save(talk2);

        member.addTalkCount();

        return "redirect:/chat/" + memberId + "/" + friendId;
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

}
