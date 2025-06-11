package com.example.iruda.api.sms;

import com.example.iruda.api.sms.dto.SmsRequest;
import com.example.iruda.api.sms.dto.SmsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    // 인증번호 발송 요청
    @PostMapping("/send")
    public ResponseEntity<?> sendSmsCode(@RequestBody SmsRequest request) {
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            return ResponseEntity.badRequest().body("전화번호를 입력해주세요.");
        }
        smsService.sendVerificationCode(request.getPhoneNumber());
        return ResponseEntity.ok("인증번호를 발송했습니다.");
    }

    // 인증번호 검증 요청
    @PostMapping("/verify")
    public ResponseEntity<SmsResponse> verifySmsCode(@RequestBody SmsRequest request) {
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty() ||
                request.getCode() == null || request.getCode().isEmpty()) {
            return ResponseEntity.badRequest().body(new SmsResponse(false, "전화번호와 인증번호를 입력해주세요."));
        }
        SmsResponse response = smsService.verifyCode(request.getPhoneNumber(), request.getCode());
        return ResponseEntity.ok(response);
    }
}
