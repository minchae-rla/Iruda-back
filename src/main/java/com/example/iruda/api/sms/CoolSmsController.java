package com.example.iruda.api.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class CoolSmsController {

    @GetMapping("/sendSMS")
    public String sendSMS (@RequestParam String u_phone) {
        Random rnd  = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<4; i++) {
            buffer.append(rnd.nextInt(10));
        }
        String cerNum = buffer.toString();
        System.out.println("수신자 번호 : " + u_phone);
        System.out.println("인증번호 : " + cerNum);
        CoolSmsService.certifiedPhoneNumber(u_phone, cerNum);
        return cerNum;
    }

}