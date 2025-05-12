package com.example.iruda.users;

import com.example.iruda.jwt.JwtProvider;
import com.example.iruda.jwt.JwtTokenDTO;
import com.example.iruda.users.dto.FindIdRequest;
import com.example.iruda.users.dto.FindPwRequest;
import com.example.iruda.users.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;  // JwtProvider 의존성 주입 추가

    // 회원 가입 처리
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserRequest userRequest) {
        userService.signup(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공했습니다.");
    }

    // 로그인 처리 및 JWT 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> login(@RequestBody UserRequest.login loginRequest) {
        JwtTokenDTO tokenDTO = userService.login(loginRequest);

        if (tokenDTO != null) {
            return ResponseEntity.ok(tokenDTO); // 로그인 성공 시 JWT 토큰 반환
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 로그인 실패 시
        }
    }

    // JWT 토큰을 사용하여 사용자를 인증하는 예시 (인증된 API 호출 시 사용)
    // 이 엔드포인트는 인증된 요청에서 호출될 수 있습니다. JWT 토큰을 검증하려면 필터를 통해 인증을 처리하세요.
    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestBody String token) {
        // JwtProvider 인스턴스를 사용하여 토큰 유효성 확인
        if (jwtProvider.validateToken(token)) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }
    
    // 아이디 중복 체크
    @PostMapping("/idCheck")
    public ResponseEntity<Boolean> idCheck(@RequestBody UserRequest userRequest) {
        boolean exists = userService.idCheck(userRequest);
        return ResponseEntity.ok(exists);
    }

    // 아이디 찾기
    @PostMapping("/findId")
    public ResponseEntity<String> findId(@RequestBody FindIdRequest findIdRequest) {
        String userId = userService.findId(findIdRequest);

        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾을 수 없습니다.");
        }
    }

    //비밀번호 찾기
    @PostMapping("/findPw")
    public ResponseEntity<String> findPw(@RequestBody FindPwRequest findPwRequest) {
        String userPw = userService.findPw(findPwRequest);

        if(userPw != null) {
            return ResponseEntity.ok(userPw);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("비밀번호를 찾을 수 없습니다.");
        }
    }

}
