package com.example.iruda.users;

import com.example.iruda.jwt.JwtGenerator;
import com.example.iruda.jwt.JwtTokenDTO;
import com.example.iruda.jwt.RefreshTokenRepository;
import com.example.iruda.jwt.dto.RefreshToken;
import com.example.iruda.projects.Project;
import com.example.iruda.projects.ProjectMember;
import com.example.iruda.projects.ProjectMemberRepository;
import com.example.iruda.projects.ProjectRepository;
import com.example.iruda.tasks.Task;
import com.example.iruda.tasks.TaskRepository;
import com.example.iruda.users.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtGenerator jwtGenerator; // JwtGenerator 의존성 주입

    // 일반 회원가입
    public void signup(UserRequest userRequest) {
        String rawPassword = userRequest.userPw();

        // password가 null 또는 빈 문자열인 경우 → OAuth 사용자로 판단하여 임시 비밀번호 생성
        if (rawPassword == null || rawPassword.isBlank()) {
            rawPassword = UUID.randomUUID().toString(); // 또는 "oauth_kakao"
        }

        String encryptedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(userRequest, encryptedPassword);
        userRepository.save(user);
    }

    // 로그인 처리 및 JWT 토큰 발급
    @Transactional
    public JwtTokenDTO login(UserRequest.login loginRequest) {
        User user = userRepository.findByUserId(loginRequest.userId());
        if (user != null && passwordEncoder.matches(loginRequest.userPw(), user.getUserPw())) {
            JwtTokenDTO tokenDTO = jwtGenerator.generateToken(user.getId());
            refreshTokenRepository.save(new RefreshToken(
                    user.getId(),
                    tokenDTO.getRefreshToken(),
                    LocalDateTime.now().plusDays(7)
            ));
            return tokenDTO;
        }
        return null;
    }

    //로그인유지확인
    @Transactional
    public JwtTokenDTO reissue(String refreshToken) {
        Optional<RefreshToken> findToken = refreshTokenRepository.findByToken(refreshToken);
        if (findToken.isEmpty()) throw new IllegalArgumentException("Invalid refresh token");

        RefreshToken storedToken = findToken.get();
        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new IllegalArgumentException("Refresh token expired");
        }

        Long userId = storedToken.getUserId();
        JwtTokenDTO newTokens = jwtGenerator.generateToken(userId);
        storedToken.setToken(newTokens.getRefreshToken());
        storedToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(storedToken);

        return newTokens;
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
    public void setPw(Long userId, SetPwRequest request) {
        String encodedPw = passwordEncoder.encode(request.userPw());
        int updated = userRepository.updatePassword(userId, encodedPw);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
        }
    }

    // 회원 탈퇴
    @Transactional
    public boolean deleteUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();

        List<ProjectMember> memberships = user.getProjectMembers();

        for (ProjectMember pm : memberships) {
            Project project = pm.getProject();
            projectRepository.delete(project);
        }

        userRepository.delete(user);

        return true;
    }
    
    
    //회원정보수정
    @Transactional
    public void updateUser(Long userId, SetUserRequest userRequest) {
        String encodedPw = null;

        if (userRequest.userPw() != null && !userRequest.userPw().isBlank()) {
            encodedPw = passwordEncoder.encode(userRequest.userPw());
        }

        SetUserRequest updatedReq = new SetUserRequest(
                userRequest.name(),
                encodedPw,
                userRequest.department(),
                userRequest.phone()
        );

        int result = userRepository.updateUser(userId, updatedReq);
        if (result == 0) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }

    //내정보조회(이름, 아이디)
    public GetMinimal findMinimalById(Long userId) {
        return userRepository.findMinimalById(userId);
    }

    //전체정보조회
    public GetUser findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

}




