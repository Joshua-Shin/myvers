package com.example.myvers.configuration;

public interface MemberConst {
    int MAXIMUM_NUMBER = 100;
    String EXCEED_MAXIMUM_NUMBER = "죄송합니다. MyVers행 열차의 승차인원이 정원 " + MAXIMUM_NUMBER + "명을 초과하게 되어 더 이상 탑승하실 수 없습니다. 조만간 더 확장된 우주로 인사드리겠습니다. 감사합니다 ㅠㅠ";
    String NOT_UNIQUE_ID = "동일한 id가 이미 존재합니다.";
    String NOT_UNIQUE_EMAIL = "해당 email로 가입한 계정이 이미 존재합니다.";
    String NOT_MATCH_CONFIRM_PASSWORD = "비밀번호가 동일하지 않습니다.";
    String NOT_MATCH_ORIGINAL_PASSWORD = "기존 비밀번호가 아닙니다.";
    String NOT_MATCH_CONFIRM_NEWPASSWORD_= "새로운 비밀번호와 확인 비밀번호가 동일하지 않습니다.";
}
