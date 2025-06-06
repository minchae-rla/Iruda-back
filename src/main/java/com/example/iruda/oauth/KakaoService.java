package com.example.iruda.oauth;

import com.example.iruda.jwt.JwtGenerator;
import com.example.iruda.jwt.JwtTokenDTO;
import com.example.iruda.oauth.client.KakaoClient;
import com.example.iruda.oauth.dto.KakaoTokenResponse;
import com.example.iruda.oauth.dto.KakaoUserInfo;
import com.example.iruda.users.User;
import com.example.iruda.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;

    public JwtTokenDTO kakaoLoginOrRegister(String code) {
        KakaoTokenResponse tokenResponse = kakaoClient.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoClient.getUserInfo(tokenResponse.getAccess_token());

        User user = userRepository.findByUserId("kakao_" + userInfo.getId());

        if (user == null) {
            user = new User();
            user.setUserId("kakao_" + userInfo.getId());
            user.setUserPw(""); // 비밀번호 없음
            user.setName(userInfo.getNickname());
            user.setPhone("010-0000-0000"); // 더미 데이터
            user.setBirth("1990-01-01");     // 더미 데이터
            user.setDepartment("소셜");      // 더미 데이터
            user.setPrivacyAgree(true);      // 카카오 약관 동의 시 true
            user.setProvider("kakao");
            user.setProviderId(String.valueOf(userInfo.getId()));
            userRepository.save(user);
        }

        return jwtGenerator.generateToken(user.getId());
    }
}
