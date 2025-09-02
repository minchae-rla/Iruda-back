package com.example.iruda.users;

import com.example.iruda.jwt.JwtProvider;
import com.example.iruda.jwt.JwtTokenDTO;
import com.example.iruda.users.dto.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

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

    //비밀번호 찾기(user가 존재하는지 확인)
    @PostMapping("/findPw")
    public ResponseEntity<Long> findPw(@RequestBody FindPwRequest findPwRequest) {
        Long id = userService.findPw(findPwRequest);

        if (id != null) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    //비밀번호 변경
    @PutMapping("/setPw")
    public ResponseEntity<String> setPw(@RequestHeader("Authorization") String authorization, @RequestBody SetPwRequest setPwRequest
    ) {
        String token = authorization.substring(7);
        Long userId = jwtProvider.getUserIdFromToken(token);

        userService.setPw(userId, setPwRequest);

        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }



    // 회원탈퇴
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7); // "Bearer " 제거
            Long userId = jwtProvider.getUserIdFromToken(token);

            boolean deleted = userService.deleteUser(userId);

            if (deleted) {
                return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
            }

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("회원 탈퇴 중 오류가 발생했습니다.");
        }
    }

    // 회원정보 수정
    @PutMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody SetUserRequest userRequest, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);
            Long userId = jwtProvider.getUserIdFromToken(token);

            userService.updateUser(userId, userRequest);

            return ResponseEntity.ok("회원정보가 성공적으로 수정되었습니다.");

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }

    //회원정보조회(아이디, 이름)
    @GetMapping("/getMinimal")
    public ResponseEntity<?> getMinimal(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);
            Long userId = jwtProvider.getUserIdFromToken(token);

            GetMinimal minimal = userService.findMinimalById(userId);
            return ResponseEntity.ok(minimal);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }

    //회원정보전체조회
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);
            Long userId = jwtProvider.getUserIdFromToken(token);

            GetUser getUser = userService.findUserById(userId);
            return ResponseEntity.ok(getUser);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }
}
