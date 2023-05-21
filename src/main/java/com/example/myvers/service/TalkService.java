package com.example.myvers.service;

import com.example.myvers.domain.Talk;
import com.example.myvers.repository.TalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TalkService {
    private final TalkRepository talkRepository;
    public Long save(Talk talk) {
        return talkRepository.save(talk);
    }
}
