package com.example.iruda.users;

import com.example.iruda.jwt.JwtGenerator;
import com.example.iruda.jwt.JwtTokenDTO;
import com.example.iruda.kakao.KakaoOAuthClient;
import com.example.iruda.kakao.dto.KakaoTokenResponse;
import com.example.iruda.kakao.dto.KakaoUserInfo;
import com.example.iruda.users.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator; // JwtGenerator 의존성 주입

    // 일반 회원가입
    public void signup(UserRequest userRequest) {
        String encryptedPassword = passwordEncoder.encode(userRequest.userPw());
        User user = new User(userRequest, encryptedPassword);
        userRepository.save(user);
    }

    // 로그인 처리 및 JWT 토큰 발급
    public JwtTokenDTO login(UserRequest.login loginRequest) {
        User user = userRepository.findByUserId(loginRequest.userId());
        if (user != null && passwordEncoder.matches(loginRequest.userPw(), user.getUserPw())) {
            // 로그인 성공 시 JWT 토큰 (accessToken, refreshToken) 생성
            return jwtGenerator.generateToken(user.getId());  // user.getId() 대신 user.getUserId() 사용
        }
        return null; // 인증 실패 시 null 반환
    }

    //아이디 중복 체크
    public boolean idCheck(UserRequest userRequest) {
        User user = userRepository.findByUserId(userRequest.userId());
        return user != null;
    }

    //아이디 찾기
    public String findId(FindIdRequest findIdRequest) {
        User user = userRepository.findByNameAndBirthAndPhone(
                findIdRequest.name(),
                findIdRequest.birth(),
                findIdRequest.phone()
        );

        if (user != null) {
            return user.getUserId();
        }
        return null;
    }

    //비밀번호 찾기
    public Long findPw(FindPwRequest findPwRequest) {
        return userRepository.findPw(
                findPwRequest.userId(),
                findPwRequest.name(),
                findPwRequest.birth(),
                findPwRequest.phone()
        );
    }

    //비밀번호 변경
    @Transactional
    public void setPw(SetPwRequest setPwRequest) {
        User user = userRepository.findById(setPwRequest.id())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        String encodedPw = passwordEncoder.encode(setPwRequest.userPw());
        user.setUserPw(encodedPw);
        userRepository.save(user);
    }

    // 카카오 회원가입
    public User kakaoSignup(UserRequest userRequest) {
        String encryptedPassword = passwordEncoder.encode(userRequest.userPw());
        User user = new User(userRequest, encryptedPassword);
        return userRepository.save(user);
    }

    @Transactional
    public JwtTokenDTO kakaoLogin(String code) {
        // 1. 카카오 서버에서 Access Token 받기
        KakaoTokenResponse tokenResponse = kakaoOAuthClient.getAccessToken(code);
        String kakaoAccessToken = tokenResponse.getAccessToken();

        // 2. 카카오 사용자 정보 가져오기
        KakaoUserInfo userInfo = kakaoOAuthClient.getUserInfo(kakaoAccessToken);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getKakaoAccount().getProfile().getNickname();
        String email = userInfo.getKakaoAccount().getEmail();

        // 3. DB에 이미 가입된 회원인지 확인
        Optional<User> existingUser = userRepository.findByProviderAndProviderId("kakao", String.valueOf(kakaoId));

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // 4. 회원이 없으면 새로 가입
            user = new User();
            user.setUserId(email);
            user.setUserPw(passwordEncoder.encode("kakao_dummy")); // 더미 비번
            user.setName(nickname);
            user.setPhone("000-0000-0000"); // 추후 입력 가능
            user.setBirth("1900-01-01");
            user.setDepartment("카카오");
            user.setPrivacyAgree(true);
            user.setProvider("kakao");
            user.setProviderId(String.valueOf(kakaoId));
            user = userRepository.save(user);
        }

        // 5. JWT 발급
        return jwtGenerator.generateToken(user.getId());
    }



}
