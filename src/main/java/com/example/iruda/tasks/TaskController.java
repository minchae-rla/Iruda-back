package com.example.iruda.tasks;

import com.example.iruda.jwt.JwtProvider;
import com.example.iruda.projects.ProjectService;
import com.example.iruda.projects.dto.ProjectResponse;
import com.example.iruda.tasks.dto.TaskRequest;
import com.example.iruda.tasks.dto.TaskResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final JwtProvider jwtProvider;

    //일정등록
    @PostMapping("/add/{projectId}")
    public ResponseEntity<String> addTask(@PathVariable Long projectId, @RequestBody TaskRequest taskRequest, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            Long userId = jwtProvider.getUserIdFromToken(token);

            boolean projectUserCheck = projectService.projectUserCheck(userId, projectId);
            if (!projectUserCheck) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 일정을 추가할 권한이 없습니다.");
            }
            taskService.addTask(taskRequest);

            return ResponseEntity.ok("일정이 성공적으로 추가되었습니다.");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }

    //일정전체조회
    @GetMapping("/getAllTask/{projectId}")
    public ResponseEntity<?> getAllTask(@PathVariable Long projectId, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);
            Long userId = jwtProvider.getUserIdFromToken(token);

            boolean projectUserCheck = projectService.projectUserCheck(userId, projectId);
            if (!projectUserCheck) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 일정을 조회할 권한이 없습니다.");
            }

            List<TaskResponse> tasks = taskService.getAllTasks(projectId);
            return ResponseEntity.ok(tasks);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    //일정상세조회
    @GetMapping("/getTask/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long taskId, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);
            Long userId = jwtProvider.getUserIdFromToken(token);

            Long projectId = projectService.taskCheck(taskId);
            boolean projectUserCheck = projectService.projectUserCheck(userId, projectId);
            if (!projectUserCheck) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            TaskResponse task = taskService.getTask(taskId);

            return ResponseEntity.ok(task);

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    //일정삭제
    @DeleteMapping("/deleteTask/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            Long userId = jwtProvider.getUserIdFromToken(token);

            Long projectId = projectService.taskCheck(taskId);

            boolean projectUserCheck = projectService.projectUserCheck(userId, projectId);
            if (!projectUserCheck) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 일정을 삭제할 권한이 없습니다.");
            }
            taskService.deleteTask(taskId);

            return ResponseEntity.ok("일정이 성공적으로 삭제되었습니다.");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }

    //일정수정
    @PutMapping("/updateTask/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest taskRequest, @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.substring(7);

            Long userId = jwtProvider.getUserIdFromToken(token);

            Long projectId = projectService.taskCheck(taskId);

            boolean projectUserCheck = projectService.projectUserCheck(userId, projectId);
            if (!projectUserCheck) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이 프로젝트를 수정할 권한이 없습니다.");
            }
            taskService.updateTask(taskId, taskRequest);

            return ResponseEntity.ok("일정이 성공적으로 수정되었습니다.");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
    }
    
    //일정알람
}
