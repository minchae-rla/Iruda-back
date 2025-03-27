package com.example.iruda.projects;

import com.example.iruda.jwt.JwtProvider;
import com.example.iruda.projects.dto.ProjectDetailRequest;
import com.example.iruda.projects.dto.ProjectRequest;
import com.example.iruda.projects.dto.ProjectResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
//@CrossOrigin("*") //프론트 백엔드 연결 우리는 webConifg파일로 미리해둠 알고만 있을 것
public class ProjectController {
    private final ProjectService projectService;
    private final JwtProvider jwtProvider;

    //유저 프로젝트 생성
    @PostMapping("/add")
    public ResponseEntity<String> addProject(@RequestBody ProjectRequest projectRequest, @RequestHeader("Authorization") String authorization) {
        try {
            // 1. Authorization 헤더에서 JWT 토큰 추출
            String token = authorization.substring(7);  // "Bearer "를 제거하여 토큰만 추출

            // 2. 토큰에서 사용자 ID 추출
            Long userId = jwtProvider.getUserIdFromToken(token);  // JwtProvider로부터 사용자 ID 추출

            // 3. 프로젝트 추가
            projectService.addProject(projectRequest, userId);

            return ResponseEntity.ok("프로젝트가 성공적으로 추가되었습니다.");

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }
    
    //프로젝트 상세 일정 등록
    @PostMapping("/addTask/{projectId}")
    public ResponseEntity<String> addTask(@PathVariable Long projectId, @RequestBody ProjectDetailRequest projectDetailRequest, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);  // "Bearer "를 제거하여 토큰만 추출

            Long userId = jwtProvider.getUserIdFromToken(token);

            boolean projectUserCheck = projectService.projectUserCheck(userId, projectId);
            if (!projectUserCheck) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 일정을 추가할 권한이 없습니다.");
            }
            projectService.addTask(projectDetailRequest, projectId);

            return ResponseEntity.ok("일정이 성공적으로 삭제되었습니다.");

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }

    //유저 프로젝트 조회(전체)
    @GetMapping("/searchAll")
    public ResponseEntity<List<ProjectResponse>> getProjects(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);  // "Bearer " 제거

            Long userId = jwtProvider.getUserIdFromToken(token);

            // 사용자가 속한 프로젝트 목록 조회
            List<ProjectResponse> projects = projectService.getProjects(userId);

            return ResponseEntity.ok(projects);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    // 프로젝트 삭제
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);  // "Bearer "를 제거하여 토큰만 추출

            Long userId = jwtProvider.getUserIdFromToken(token);

            boolean projectUserCheck = projectService.projectUserCheck(userId, projectId);
            if (!projectUserCheck) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 프로젝트를 삭제할 권한이 없습니다.");
            }
            projectService.deleteProject(projectId);

            return ResponseEntity.ok("프로젝트가 성공적으로 삭제되었습니다.");

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }

    //프로젝트 수정
    @PutMapping("/update/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest projectRequest, @RequestHeader("Authorization") String authorization) {
        try{
            String token = authorization.substring(7);

            long userId = jwtProvider.getUserIdFromToken(token);

            boolean projectUserCheck = projectService.projectUserCheck(userId, projectId);
            if (!projectUserCheck) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 프로젝트를 수정할 권한이 없습니다.");
            }
            projectService.updateProject(projectId, projectRequest);

            return ResponseEntity.ok("프로젝트가 성공적으로 수정되었습니다.");

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

    }


}
