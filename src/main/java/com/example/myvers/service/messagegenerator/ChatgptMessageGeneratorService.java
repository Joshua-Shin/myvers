package com.example.myvers.service.messagegenerator;

import com.example.myvers.domain.Speaker;
import com.example.myvers.domain.Talk;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatgptMessageGeneratorService implements MessageGeneratorService{
    private final ChatgptService chatgptService;

    public String receiveMessage(List<Talk> talks) {
        List<MultiChatMessage> messages = new ArrayList<>();
        for(Talk talk: talks) { // 모든 대화내용을 다 보낼 수는 없고, 최신 100개만 보내는게 나을듯.
            if(talk.getSpeaker() == Speaker.AI) {
                messages.add(new MultiChatMessage("assistant",talk.getMessage()));
            } else if(talk.getSpeaker() == Speaker.HUMAN) {
                messages.add(new MultiChatMessage("user",talk.getMessage()));
            } else {
                messages.add(new MultiChatMessage("system",talk.getMessage()));
            }
        }
        return chatgptService.multiChat(messages);
    }
}
