package com.example.myvers.service;

import com.example.myvers.service.messagegenerator.MessageGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageGeneratorServiceTest {

    @Autowired
    MessageGeneratorService messageGeneratorService;

    @Test
    void sendMessage() {
//        String response = messageGeneratorService.sendMessage("내 이름이 뭐라고 했지?");
//        System.out.println(response);
    }

}