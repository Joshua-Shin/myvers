package com.example.myvers.service.messagegenerator;

import com.example.myvers.domain.Talk;
import java.util.List;

public interface MessageGeneratorService {

    String receiveMessage(List<Talk> talks);

}
