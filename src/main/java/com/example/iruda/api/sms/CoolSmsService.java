package com.example.iruda.api.sms;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CoolSmsService {
    public void certifiedPhoneNumber(String u_phone, String cerNum) {
        String api_key = "본인의 API KEY";
        String api_secret = "본인의 API SECRET";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", u_phone); // 수신전화번호
        params.put("from", "010-0000-0000"); // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "[BitMovie] 문자 본인인증 서비스 : 인증번호는 " + "[" + cerNum + "]" + " 입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
