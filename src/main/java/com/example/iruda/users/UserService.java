package com.example.iruda.users;

import com.example.iruda.jwt.JwtGenerator;
import com.example.iruda.jwt.JwtTokenDTO;
import com.example.iruda.users.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator; // JwtGenerator 의존성 주입

    // 회원 가입 처리
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




}
