package com.example.iruda.api.sms;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

    private final DefaultMessageService messageService;

    @Value("${coolsms.from.number}")
    private String fromNumber;

    public SmsUtil(@Value("${coolsms.api.key}") String apiKey,
                   @Value("${coolsms.api.secret}") String apiSecret) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public boolean sendSms(String toNumber, String text) {
        // 1. Message 객체 만들기
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText(text);

        // 2. Message를 SingleMessageSendingRequest에 넣어서 생성
        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);

        try {
            SingleMessageSentResponse response = messageService.sendOne(request);
            System.out.println("Response status: " + response.getStatusCode());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
