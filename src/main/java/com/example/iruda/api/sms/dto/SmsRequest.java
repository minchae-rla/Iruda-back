package com.example.iruda.api.sms.dto;

public class SmsRequest {
    private String phoneNumber;
    private String code;  // 인증번호 검증 시 사용

    public SmsRequest() {}

    public SmsRequest(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
