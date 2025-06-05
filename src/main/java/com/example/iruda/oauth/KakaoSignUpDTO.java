package com.example.iruda.oauth;

import lombok.Getter;

@Getter
public class KakaoSignUpDTO {

    private String userId;
    private String name;
    private String phone;
    private String birth;
    private String department;
    private boolean privacyAgree;

    // 프론트에서 JSON으로 받으므로 기본 생성자, setter 있어야 함
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setPrivacyAgree(boolean privacyAgree) {
        this.privacyAgree = privacyAgree;
    }
}
