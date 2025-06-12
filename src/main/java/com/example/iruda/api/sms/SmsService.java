package com.example.iruda.api.sms;

import com.example.iruda.api.sms.dto.SmsResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SmsService {

    private final SmsUtil smsUtil;

    // 전화번호 별로 인증번호 임시 저장
    private Map<String, String> verificationCodes = new HashMap<>();

    // 생성자를 통한 의존성 주입
    public SmsService(SmsUtil smsUtil) {
        this.smsUtil = smsUtil;
    }

    // 인증번호 생성 (6자리 숫자)
    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // 인증번호 발송
    public void sendVerificationCode(String phoneNumber) {
        String code = generateCode();
        verificationCodes.put(phoneNumber, code);
        String message = "[IRUDA] 인증번호는 " + code + "입니다.";
        smsUtil.sendSms(phoneNumber, message);
    }

    // 인증번호 검증
    public SmsResponse verifyCode(String phoneNumber, String code) {
        String savedCode = verificationCodes.get(phoneNumber);
        if (savedCode == null) {
            return new SmsResponse(false, "인증번호를 발송해주세요.");
        }
        if (savedCode.equals(code)) {
            verificationCodes.remove(phoneNumber);
            return new SmsResponse(true, "인증에 성공했습니다.");
        } else {
            return new SmsResponse(false, "인증번호가 일치하지 않습니다.");
        }
    }
}
